package com.oneeuros.onelog.comment;

import com.oneeuros.onelog.common.PasswordValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;



    @Transactional
    public Comment save(String nickname, String password, String content) {
        PasswordValidator.validatePassword(password);
        Comment comment = new Comment(nickname, password, content);
        return commentRepository.save(comment);
    }
}
