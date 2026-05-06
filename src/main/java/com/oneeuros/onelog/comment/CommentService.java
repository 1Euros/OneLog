package com.oneeuros.onelog.comment;

import com.oneeuros.onelog.comment.dto.CommentCreateRequestDto;
import com.oneeuros.onelog.common.PasswordUtils;
import com.oneeuros.onelog.post.Post;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;


    // 댓글 저장
    @Transactional
    public void save(Post post, CommentCreateRequestDto requestDto) {
        // 비밀번호 인코딩
        String encodedPassword = PasswordUtils.encodePassword(requestDto.password());

        // 데이터 저장
        Comment comment = new Comment(requestDto.nickname(), encodedPassword, requestDto.content(), post);
        commentRepository.save(comment);
    }

    // 댓글 목록 조회
    public List<Comment> findComments(Long postId) {
        return commentRepository.findAllByPostIdOrderByCreatedAtDesc(postId);

    }

    // 해당 게시글의 최신순으로 첫번째 댓글 조회
    public Comment findFirstComment(Long postId) {
        return commentRepository.findFirstByPostIdOrderByCreatedAtDesc(postId).orElse(null);
    }

    //해당 게시글의 댓글 수 조회
    public int countCommentsByPostId(Long postId) {
        return commentRepository.countByPostId(postId);
    }

    //수정/삭제시 비밀번호 확인
    public Boolean confirmPassword(Long commentId, String password) {
        //댓글 존재 확인
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("해당하는 댓글이 존재하지 않습니다."));

        // 비밀번호 확인
        return PasswordUtils.checkPassword(password, comment.getPassword());
    }
}
