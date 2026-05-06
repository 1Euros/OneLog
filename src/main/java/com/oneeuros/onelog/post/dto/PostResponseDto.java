package com.oneeuros.onelog.post.dto;

import com.oneeuros.onelog.comment.Comment;
import com.oneeuros.onelog.post.Post;

public record PostResponseDto(
        Post post,
        Comment comment,
        int commentCount
) {


}
