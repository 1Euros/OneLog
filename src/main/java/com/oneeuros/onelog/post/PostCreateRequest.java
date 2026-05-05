package com.oneeuros.onelog.post;

// Request DTO
// 게시글 생성요청시 받는 값들을 정리
public record PostCreateRequest (
    String nickname,
    String password,
    String title,
    String content,

    Long boardId
){

}
