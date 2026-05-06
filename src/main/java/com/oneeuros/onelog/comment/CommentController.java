package com.oneeuros.onelog.comment;

import com.oneeuros.onelog.comment.dto.CommentCreateRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/post/{postId}/create")
    public String saveComment(@PathVariable Long postId, @Valid CommentCreateRequestDto requestDto, BindingResult bindingResult, Model model) {
        // 유효성 검증
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
        else  commentService.save(postId, requestDto);
        List<Comment> comments = commentService.findComments(postId);
        model.addAttribute("comments",comments);
        return "comments/comments";
    }

    // 댓글 목록 조회
    @GetMapping("/post/{postId}/comments")
    public String findComments(@PathVariable Long postId, Model model) {
        List<Comment> comments = commentService.findComments(postId);
        model.addAttribute("comments",comments);
        return "comments/comments";
    }

    // 첫번째 댓글, 댓글수 테스트
    @GetMapping("/post/{postId}/comments/summary")
    public String commentSummary(@PathVariable Long postId, Model model) {
        model.addAttribute("comment",commentService.findFirstComment(postId));
        model.addAttribute("count",commentService.countCommentsByPostId(postId));
        return "comments/comment";
    }
}
