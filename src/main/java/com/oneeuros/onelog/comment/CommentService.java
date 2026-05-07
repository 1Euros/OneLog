package com.oneeuros.onelog.comment;

import com.oneeuros.onelog.comment.dto.CommentCreateRequestDto;
import com.oneeuros.onelog.comment.dto.CommentUpdateRequestDto;
import com.oneeuros.onelog.common.PasswordUtils;
import com.oneeuros.onelog.post.Post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;


    // 댓글 저장
    @Transactional
    public void save(Post post, CommentCreateRequestDto request) {
        // 비밀번호 인코딩
        String encodedPassword = PasswordUtils.encodePassword(request.password());
        // 부모 댓글이 없는 경우 초기화
        int depth = 0;
        Comment parent = null;
        Long groupId = 0L;
        int newOrder = 0;   // 새로운 순서

        // 부모 댓글이 있다면 해당 댓글 찾기
        if (request.parentId()!= null) {
            parent = commentRepository.findById(request.parentId())
                    .orElseThrow(() -> new IllegalArgumentException("해당하는 댓글이 없습니다"));
            depth = parent.getDepth()+1;
            groupId = parent.getGroupId();


            // 부모 서브트리의 다음 서브트리의 첫 시작점
            Integer boundary = commentRepository
                    .findSubtreeBoundary(groupId,parent.getGroupOrder(),parent.getDepth());

            // 만약 다음 서브트리가 없다면 마지막 순서  뒤로 넣기
            if (boundary == null) {
                int maxOrder = commentRepository.findMaxOrder(parent.getGroupId());
                newOrder = maxOrder + 1;

            // 다음 서브트리가 있다면
            }else {
                // boundary 앞에 삽입
                newOrder = boundary;
                // boundary 부터 같은 그룹 데이터 뒤로 밀기
                List<Comment> nextComments = commentRepository.findByGroupIdAndGroupOrderGreaterThanEqual(groupId, boundary);
                nextComments.forEach(c -> c.pushGroupOrder());
            }
        }

        // 데이터 저장
        Comment comment = new Comment(
                request.nickname(),
                encodedPassword,
                request.content(),
                post,
                parent, depth,groupId, newOrder);

        commentRepository.save(comment);
        // 최상위 댓글이라면 댓글 groupId 다시 넣어주기
        if (groupId == 0L) comment.registerGroupId(comment.getId());
    }

    // 댓글 목록 조회
    @Transactional(readOnly = true)
    public List<Comment> findComments(Long postId) {
        return commentRepository.findAllByPostIdOrderByCreatedAtDesc(postId);

    }

    // 댓글 수정
    @Transactional
    public void updateComment(Long commentId, CommentUpdateRequestDto request) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("해당하는 아이디의 댓글이 없습니다."));
        comment.updateContent(request.content());
    }


    // 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("해당하는 아이디의 댓글이 없습니다."));
        commentRepository.deleteById(commentId);
    }

    // 해당 게시글의 최신순으로 첫번째 댓글 조회
    @Transactional(readOnly = true)
    public Comment findFirstComment(Long postId) {
        return commentRepository.findFirstByPostIdOrderByCreatedAtDesc(postId).orElse(null);
    }

    //해당 게시글의 댓글 수 조회
    @Transactional(readOnly = true)
    public int countCommentsByPostId(Long postId) {
        return commentRepository.countByPostId(postId);
    }

    //수정/삭제시 비밀번호 확인
    @Transactional(readOnly = true)
    public Boolean confirmPassword(Long commentId, String password) {
        //댓글 존재 확인
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("해당하는 댓글이 존재하지 않습니다."));

        // 비밀번호 확인
        return PasswordUtils.checkPassword(password, comment.getPassword());
    }
}
