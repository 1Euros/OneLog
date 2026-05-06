package com.oneeuros.onelog.comment;

import com.oneeuros.onelog.comment.dto.CommentCreateRequestDto;
import com.oneeuros.onelog.comment.dto.CommentUpdateRequestDto;
import com.oneeuros.onelog.post.Post;
import com.oneeuros.onelog.post.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;

    @PostMapping("/post/{postId}/create")
    public String saveComment(@PathVariable Long postId, @Valid CommentCreateRequestDto request, BindingResult bindingResult, Model model) {
        // 유효성 검증
        // 해당 포스트 존재 여부 검증
        Post post = postService.findByIdForComment(postId);

        // 닉네임, 비밀번호, 내용 검증
        if (bindingResult.hasErrors()) {
            if (bindingResult.hasFieldErrors("nickname")) {
                var errors = bindingResult.getFieldErrors("nickname");
                for (var error : errors) {
                    String code = error.getCode();
                    if ("NotBlank".equals(code))
                        model.addAttribute("errorMessage", "닉네임을 입력해주세요.");
                    else if ("Size".equals(code))
                        model.addAttribute("errorMessage", "닉네임은 30자 이하입니다.");
                }
            }
            if (bindingResult.hasFieldErrors("password"))
                model.addAttribute("errorMessage", "비밀번호는 숫자 6자리여야 합니다.");
            if (bindingResult.hasFieldErrors("content"))
                model.addAttribute("errorMessage", "내용을 입력해주세요.");
        }
        else  commentService.save(post, request);
        return "redirect:/comment/post/%s/comments".formatted(postId);
    }

    // 댓글 목록 조회
    @GetMapping("/post/{postId}/comments")
    public String findComments(@PathVariable Long postId, Model model) {
        List<Comment> comments = commentService.findComments(postId);
        model.addAttribute("comments",comments);
        return "comments/comments";
    }

    // 댓글 수정
    @PostMapping("/{commentId}/update")
    public String updateComment(@PathVariable Long commentId,
                                @Valid CommentUpdateRequestDto request,
                                BindingResult bindingResult,
                                @RequestParam Long postId,
                                Model model
) {
        if (bindingResult.hasFieldErrors("content")) {
            var errors = bindingResult.getFieldErrors("content");
            for (var error : errors) {
                String code = error.getCode();
                if ("NotBlank".equals(code)) {
                    model.addAttribute("errorMessage", "내용을 입력해주세요.");
                    model.addAttribute("postId", postId);
                }
            }
            return "comments/edit-comment";
        }
        // 수정 후 해당 댓글의 게시글 id 반환
        commentService.updateComment(commentId, request);
        return "redirect:/comment/post/%s/comments".formatted(postId);
    }

    // 첫번째 댓글, 댓글수 테스트
    @GetMapping("/post/{postId}/comments/summary")
    public String commentSummary(@PathVariable Long postId, Model model) {
        model.addAttribute("comment",commentService.findFirstComment(postId));
        model.addAttribute("count",commentService.countCommentsByPostId(postId));
        return "comments/comment";
    }
}
