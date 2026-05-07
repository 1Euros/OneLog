package com.oneeuros.onelog.common;

import com.oneeuros.onelog.board.Board;
import com.oneeuros.onelog.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {
    private final BoardService boardService;

    @ModelAttribute("sidebarBoards")
    public List<Board> boards() {
        return boardService.findAllBoards();
    }
}
