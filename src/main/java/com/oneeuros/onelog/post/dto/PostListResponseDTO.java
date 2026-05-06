package com.oneeuros.onelog.post.dto;

import java.time.LocalDateTime;

// 전체 게시글 목록 화면에 보여줄 데이터 모음
public record PostListResponseDTO(
        // 화면에 필요한 값만 담는 용도
        Long postId,
        Long boardId,
        String boardName,
        String title,
        String nickname,
        int viewCount,
        long commentCount,
        LocalDateTime createdAt
) {

}
