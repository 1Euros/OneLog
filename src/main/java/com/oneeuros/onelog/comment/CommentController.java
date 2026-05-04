package com.oneeuros.onelog.comment;

import com.oneeuros.onelog.comment.dto.CommentCreateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String saveComment(CommentCreateRequestDto requestDto, Model model) {
        Comment comment = commentService.save(requestDto);
        model.addAttribute("comment", comment);
        return "comments/comments";
    }
}
