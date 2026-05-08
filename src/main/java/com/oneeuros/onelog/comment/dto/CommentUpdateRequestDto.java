package com.oneeuros.onelog.comment.dto;

import jakarta.validation.constraints.NotBlank;

public record CommentUpdateRequestDto(
        @NotBlank(message = "내용을 입력해주세요.")
        String content
) {
}
