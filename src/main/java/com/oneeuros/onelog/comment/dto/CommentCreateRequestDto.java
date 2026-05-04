package com.oneeuros.onelog.comment.dto;

public record CommentCreateRequestDto(
        String nickname,
        String password,
        String content
) {
}
