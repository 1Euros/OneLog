package com.oneeuros.onelog.post;

//import com.oneeuros.onelog.common.PasswordValidator;
import com.oneeuros.onelog.common.PasswordUtils;
import com.oneeuros.onelog.post.dto.PostCreateRequestDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    // post로 받는 save()
    @Transactional
    public Post save(PostCreateRequestDTO request) {

        //비밀번호 인코딩
        String encodedPassword = PasswordUtils.encodePassword(request.password());

        // post 저장
        Post post = new Post(
                request.title(),
                request.content(),
                request.nickname(),
                request.password());
        return postRepository.save(post);
    }


}
