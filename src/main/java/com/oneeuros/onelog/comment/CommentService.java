package com.oneeuros.onelog.comment;

import com.oneeuros.onelog.comment.dto.CommentCreateRequestDto;
import com.oneeuros.onelog.common.PasswordUtils;
import com.oneeuros.onelog.post.Post;
import com.oneeuros.onelog.post.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;


    // 댓글 저장
    @Transactional
    public void save(Long postId, CommentCreateRequestDto requestDto) {
        //유효성 검사
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 아이디의 게시글이 없습니다."));

        // 비밀번호 인코딩
        String encodedPassword = PasswordUtils.encodePassword(requestDto.password());

        // 데이터 저장
        Comment comment = new Comment(requestDto.nickname(), encodedPassword, requestDto.content(), post);
        commentRepository.save(comment);
    }

    // 댓글 목록 조회
    public List<Comment> findComments(Long postId) {
        // 해당 게시글 있는지 확인
        postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("해당 아이디의 게시글이 없습니다."));

        return commentRepository.findAllByPostIdOrderByCreatedAtDesc(postId);

    }

    public Comment findFirstComment(Long postId) {
        return commentRepository.findFirstByPostIdOrderByCreatedAtDesc(postId).orElse(null);
    }

    public int countCommentsByPostId(Long postId) {
        return commentRepository.countByPostId(postId);
    }
}
