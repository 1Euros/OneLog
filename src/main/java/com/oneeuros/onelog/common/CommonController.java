package com.oneeuros.onelog.common;

import com.oneeuros.onelog.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.regex.Pattern;

@Controller
@RequiredArgsConstructor
public class CommonController {
    private final CommentService commentService;

    // 비밀번호 확인 창 열기
    @GetMapping("/confirm/password/{domain}/{domainId}")
    public String showConfirmPassword (@PathVariable String domain,
                                        @PathVariable Long domainId,
                                        Model model) {
        model.addAttribute("domain", domain);
        model.addAttribute("domainId",domainId);
        return "common/confirm-password";
    }

    // 비밀번호 확인
    @PostMapping("/confirm/password/{domain}/{domainId}/{action}")
    public String confirmPassword(@PathVariable String domain,
                                  @PathVariable Long domainId,
                                  @PathVariable String action,
                                  @RequestParam String password,
                                  Model model
                                  ) {
        PasswordDomain pd = PasswordDomain.validDomain(domain);
        PasswordAction pa = PasswordAction.validAction(action);

        //domain과 action에 따라 분기
        if (pd == null && pa == null) throw new IllegalArgumentException("잘못된 경로입니다.");
        // 게시글일때 게시글 서비스의 수정/삭제 메서드로 이동
        if (pd == PasswordDomain.POST) {
            if (pa == PasswordAction.EDIT) {
                return null;
            }else {
                return null;
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
                return null;
            }else {
                return null;
            }
        }
    }

}
