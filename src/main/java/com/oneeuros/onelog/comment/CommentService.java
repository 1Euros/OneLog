package com.oneeuros.onelog.comment;

import com.oneeuros.onelog.comment.dto.CommentCreateRequestDto;
import com.oneeuros.onelog.common.PasswordUtils;
import com.oneeuros.onelog.post.Post;
import com.oneeuros.onelog.post.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;


    // 댓글 저장
    @Transactional
    public Comment save(Long postId, CommentCreateRequestDto requestDto) {
        //유효성 검사
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 아이디의 게시글이 없습니다."));

        // 비밀번호 인코딩
        String encodedPassword = PasswordUtils.encodePassword(requestDto.password());

        // 데이터 저장
        Comment comment = new Comment(requestDto.nickname(), encodedPassword, requestDto.content(), post);
        return commentRepository.save(comment);
    }
}
