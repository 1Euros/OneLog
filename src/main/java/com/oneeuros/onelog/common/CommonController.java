package com.oneeuros.onelog.common;

import com.oneeuros.onelog.board.Board;
import com.oneeuros.onelog.board.BoardService;
import com.oneeuros.onelog.comment.CommentService;
import com.oneeuros.onelog.post.Post;
import com.oneeuros.onelog.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CommonController {
    private final CommentService commentService;
    private final PostService postService;
    private final BoardService boardService;

    // 비밀번호 확인 창 열기
    @GetMapping("/confirm/password/{domain}/{domainId}")
    public String showConfirmPassword (@PathVariable String domain,
                                        @PathVariable Long domainId,
                                        @RequestParam(required = false) Long postId,
                                        Model model) {
        model.addAttribute("domain", domain);
        model.addAttribute("domainId",domainId);

        // 공통 비밀번호 확인해서 postid값을 항상 사용할 수 있게함.
        if (postId == null && PasswordDomain.validDomain(domain) == PasswordDomain.POST) {
            postId = domainId;
        }

        model.addAttribute("postId",postId);
        return "common/confirm-password";
    }

    // 비밀번호 확인
    @PostMapping("/confirm/password/{domain}/{domainId}/{action}")
    public String confirmPassword(@PathVariable String domain,
                                  @PathVariable Long domainId,
                                  @PathVariable String action,
                                  @RequestParam String password,
                                  @RequestParam(required = false) Long postId,
                                  Model model
                                  ) {
        PasswordDomain pd = PasswordDomain.validDomain(domain);
        PasswordAction pa = PasswordAction.validAction(action);

        //domain과 action에 따라 분기
        if (pd == null || pa == null) throw new IllegalArgumentException("잘못된 경로입니다.");
        // 게시글일때 게시글 서비스의 수정/삭제 메서드로 이동
        if (pd == PasswordDomain.POST) {
            Boolean isTrue = postService.confirmPassword(domainId, password);

            if (!isTrue) {
                model.addAttribute("domain", domain);
                model.addAttribute("domainId", domainId);
                model.addAttribute("postId", domainId);
                model.addAttribute("errorMessage", "비밀번호가 틀렸습니다.");
                return "common/confirm-password";
            }

            // 비밀번호 맞을 시 수정/삭제로 분기해서 해당 창으로 이동
            if (pa == PasswordAction.EDIT) {
                // 수정 후 상세페이지로 이동
                Post post = postService.findPostForEdit(domainId);
                List<Board> boards = boardService.findAllBoards();

                model.addAttribute("post", post);
                model.addAttribute("boards", boards);

                return "posts/edit-post";
            }

            try {
                // 삭제 후 전체 목록으로 이동
                postService.deletePost(domainId);
                return "redirect:/post";
            } catch (IllegalArgumentException e) {
                model.addAttribute("errorMessage", e.getMessage());
                return "posts/errortest";
            }
            // 댓글일 때 댓글 서비스의 수정/삭제 메서드로 이동
        }else {
            // 비밀번호 확인
            Boolean istrue = commentService.confirmPassword(domainId, password);
            if (!istrue) {
                model.addAttribute("errorMessage", "비밀번호가 틀렸습니다.");
                return "common/confirm-password";
            }
            // 비밀번호 맞을시 수정/삭제로 분기해서 해당창으로 이동
            if (pa == PasswordAction.EDIT) {
                //수정창으로 이동
                model.addAttribute("commentId", domainId);
                model.addAttribute("postId", postId);
                return "comments/edit-comment";
            }else {
                // 삭제 후 댓글 목록으로 이동
                commentService.deleteComment(domainId);
                return "redirect:/comment/post/%s/comments".formatted(postId);
            }
        }
    }

}
