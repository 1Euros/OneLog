package com.oneeuros.onelog.comment.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Valid
public record CommentCreateRequestDto(
        @NotBlank(message = "닉네임을 입력해주세요.")
        @Size(max = 30, message = "닉네임은 30자 이하입니다.")
        String nickname,
        @Pattern(regexp = "^[0-9]{6}$")
        String password,
        @NotBlank(message = "내용을 입력해주세요.")
        String content
) {
}
