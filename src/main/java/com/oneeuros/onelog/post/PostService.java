package com.oneeuros.onelog.post;

import com.oneeuros.onelog.common.PasswordValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    // post로 받는 save()
    public Post save(PostCreateRequest request) {
        // 닉네임 필수값 검증
        validateNickname(request.nickname());

        PasswordValidator.validatePassword(request.password());
        Post post = new Post(request.title(), request.content(), request.nickname(), request.password());
        return postRepository.save(post);
    }


    private void validateNickname(String nickname) {
        if (nickname == null || nickname.trim().isEmpty()) {
            throw new IllegalArgumentException("닉네임은 필수입니다.");
        }
    }


    /*// Get으로 받는 post create test
    @Transactional
    public Post save(String title, String content, String nickname, String password){
        PasswordValidator.validatePassword(password);
        Post post = new Post(title, content, nickname, password);
        return postRepository.save(post);
    }*/

}
