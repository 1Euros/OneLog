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
    public Post save(String title, String content, String nickname, String password){
        PasswordValidator.validatePassword(password);
        Post post = new Post(title, content, nickname, password);
        return postRepository.save(post);
    }


    /*// Get으로 받는 post create test
    @Transactional
    public Post save(String title, String content, String nickname, String password){
        PasswordValidator.validatePassword(password);
        Post post = new Post(title, content, nickname, password);
        return postRepository.save(post);
    }*/
}
