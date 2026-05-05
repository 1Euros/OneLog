package com.oneeuros.onelog.comment;

import com.oneeuros.onelog.comment.dto.CommentCreateRequestDto;
import com.oneeuros.onelog.common.PasswordUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;


    // 댓글 저장
    @Transactional
    public Comment save(CommentCreateRequestDto requestDto) {
        // 비밀번호 인코딩
        String encodedPassword = PasswordUtils.encodePassword(requestDto.password());

        // 데이터 저장
        Comment comment = new Comment(requestDto.nickname(), encodedPassword, requestDto.content());
        return commentRepository.save(comment);
    }
}
