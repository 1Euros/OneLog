package com.oneeuros.onelog.post;

import com.oneeuros.onelog.board.Board;
import com.oneeuros.onelog.board.BoardRepository;
import com.oneeuros.onelog.comment.CommentService;
import com.oneeuros.onelog.post.dto.PostCreateRequestDTO;
import com.oneeuros.onelog.post.dto.PostListResponseDTO;
import com.oneeuros.onelog.post.dto.PostResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.oneeuros.onelog.common.PasswordUtils;



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

        // 비밀번호 인코딩
        String encodedPassword = PasswordUtils.encodePassword(request.password());

        // post 저장
        Post post = new Post(
                request.title(),
                request.content(),
                request.nickname(),
                encodedPassword, board);
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

    // 전체 게시글 목록 불러오기 [15개씩 페이지처리]
    public Page<PostListResponseDTO> findPostList(int page){
        Pageable pageable = PageRequest.of(page,4);

        Page<Post> posts = postRepository.findAllByOrderByCreatedAtDesc(pageable);

        // Page<Post>를 Page<PostListResponseDTO>로 변경하기
        return posts.map(post -> new PostListResponseDTO(
                post.getId(),
                post.getTitle(),
                post.getNickname(),
                post.getViewCount(),
                commentService.countCommentsByPostId(post.getId()),
                post.getCreatedAt()
        ));
    }


}
