package com.oneeuros.onelog.post;

//import com.oneeuros.onelog.common.PasswordValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    // post로 받는 save()
    @Transactional
    public Post save(PostCreateRequest request) {
        // 닉네임 필수값 검증
        validateNickname(request.nickname());

        // 비밀번호 필수값 검증
        validatePasswordRequired(request.password());

        // 비밀번호 숫자 6자리 검증
        //PasswordValidator.validatePassword(request.password());  // 임의 주석 처리

        // 제목 필수값 검증
        validateTitle(request.title());

        // 내용 필수값 검증
        validateContent(request.content());

        Post post = new Post(request.title(), request.content(), request.nickname(), request.password());
        return postRepository.save(post);
    }

    // 닉네임 필수값 검증 메서드
    private void validateNickname(String nickname) {
        if (nickname == null || nickname.trim().isEmpty()) {
            throw new IllegalArgumentException("닉네임은 필수입니다.");
        }
    }
    // 비밀번호 필수값 검증 메서드
    private void validatePasswordRequired(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("비밀번호는 필수입니다.");
        }
    }
    // 제목 필수값 검증 메서드
    private void validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("제목은 필수입니다.");
        }
    }
    // 내용 필수값 검증 메서드
    private void validateContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("내용은 필수입니다.");
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
