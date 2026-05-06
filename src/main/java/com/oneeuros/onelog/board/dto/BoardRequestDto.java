package com.oneeuros.onelog.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BoardRequestDto(
        @NotBlank(message = "게시판 이름은 필수입니다.")
        @Size(max = 30, message = "게시판 이름은 30자 이하입니다.")
        String name
) {
}
