package com.oneeuros.onelog.post.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// Request DTO
// 게시글 생성요청시 받는 값들을 정리
@Valid
public record PostCreateRequestDTO(
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(max = 30, message = "닉네임은 30자 이하입니다.")
    String nickname,

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^[0-9]{6}$", message = "비밀번호는 숫자 6자리여야 합니다.")
    String password,

    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 100, message = "제목은 100자 이하입니다.")
    String title,

    @NotBlank(message = "내용을 입력해주세요.")
    String content,

    Long boardId
){

}
