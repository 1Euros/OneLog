package com.oneeuros.onelog.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;

    //post 방식으로 수정
    @PostMapping ("/create")
    public String saveBoard(
            @RequestParam String name,
            Model model) {
        if (name == null || name.trim().isEmpty()) {
            throw new RuntimeException("게시판 이름은 필수입니다");
        }
        Board board = boardService.save(name.trim());
        model.addAttribute("board", board);
        return "boards/boards";
    }

    // Get 방식
//    @GetMapping ("/create")
//    public String saveBoard(String name, Model model) {
//        if (name == null || name.trim().isEmpty()) {
//            throw new RuntimeException("게시판 이름은 필수입니다");
//        }
//        Board board = boardService.save(name.trim());
//        model.addAttribute("board", board);
//        return "boards/boards";
//    }

}
