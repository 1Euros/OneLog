package com.oneeuros.onelog.board;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    @Transactional
    public Board save(String name){
        if (name == null || name.trim().isEmpty()) {
            throw new RuntimeException("게시판 이름은 필수입니다");
        }
        name = name.trim();
        if (boardRepository.existsByName(name)) {
            throw new IllegalArgumentException("이미 존재하는 게시판입니다");
        }
        Board board = new Board(name);
        return boardRepository.save(board);
    }
}
