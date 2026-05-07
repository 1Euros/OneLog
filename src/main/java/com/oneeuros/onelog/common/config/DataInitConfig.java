package com.oneeuros.onelog.common.config;

import com.oneeuros.onelog.board.Board;
import com.oneeuros.onelog.board.BoardRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitConfig {

    @Bean
    CommandLineRunner initBoard(BoardRepository boardRepository) {
        return args -> {
            if (boardRepository.count() == 0) { // 중복 방지
                Board board = new Board("기본 게시판");
                boardRepository.save(board);
            }
        };
    }
}