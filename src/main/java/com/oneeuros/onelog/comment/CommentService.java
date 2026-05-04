package com.oneeuros.onelog.comment;

import com.oneeuros.onelog.common.PasswordValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;



    // 댓글 저장
    @Transactional
    public Comment save(String nickname, String password, String content) {
        // 유효성 검증
        // 닉네임 검증
        if(nickname == null || nickname.trim().isEmpty()) throw new IllegalArgumentException("닉네임은 빈칸이 불가능합니다.");
        else if (nickname.length() > 30) throw new IllegalArgumentException("닉네임은 30자 이하만 가능합니다");
        // 내용 빈칸 검증
        if(content == null || content.trim().isEmpty()) throw new IllegalArgumentException("내용은 빈칸이 불가능합니다.");

        PasswordValidator.validatePassword(password);

        // 데이터 저장
        Comment comment = new Comment(nickname, password, content);
        return commentRepository.save(comment);
    }
}
