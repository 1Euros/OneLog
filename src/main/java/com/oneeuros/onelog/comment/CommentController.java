package com.oneeuros.onelog.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/create")
    public String saveComment(String nickname, String password, String content, Model model) {
        Comment comment = commentService.save(nickname, password, content);
        model.addAttribute("comment", comment);
        return "comments/comments";
    }
}
