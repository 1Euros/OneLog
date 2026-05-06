package com.oneeuros.onelog.post;

//import com.oneeuros.onelog.common.PasswordValidator;
import com.oneeuros.onelog.board.Board;
import com.oneeuros.onelog.board.BoardRepository;
import com.oneeuros.onelog.comment.CommentService;
import com.oneeuros.onelog.common.PasswordUtils;
import com.oneeuros.onelog.post.dto.PostCreateRequestDTO;
import com.oneeuros.onelog.post.dto.PostResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final BoardRepository boardRepository;
    private final CommentService commentService;


    // post로 받는 save()
    @Transactional
    public Post save(PostCreateRequestDTO request) {


        Board board = boardRepository.findById(request.boardId()).orElseThrow(()->new IllegalArgumentException("해당 board id가 없습니다."));

        // post 저장
        Post post = new Post(
                request.title(),
                request.content(),
                request.nickname(),
                request.password(), board);
        return postRepository.save(post);
    }

        // 게시글 상세 조회
        @Transactional
        public PostResponseDTO findById(Long postId) {
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

            post.increaseViewCount();

            return new PostResponseDTO(
                    post,
                    commentService.findFirstComment(postId),
                    commentService.countCommentsByPostId(postId)
            );
        }


        // postId를 이용한 게시판 조회
    public Post findByIdForComment(Long postId){
        return postRepository.findById(postId).orElseThrow(()->new IllegalArgumentException("게시글을 찾을 수 없습니다."));
    }


}
