package com.oneeuros.onelog.comment;

import com.oneeuros.onelog.comment.dto.CommentCreateRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/show/input")
    public String showInputComment() {
        return "comments/showInputComment";
    }

    @PostMapping("/create")
    public String saveComment(@Valid CommentCreateRequestDto requestDto, BindingResult bindingResult,  Model model) {
        // 유효성 검증
        if (bindingResult.hasFieldErrors("nickname"))  {
            var errors = bindingResult.getFieldErrors("nickname");
            for (var error:errors) {
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

        Comment comment = commentService.save(requestDto);
        model.addAttribute("comment", comment);
        return "comments/comments";
    }
}
