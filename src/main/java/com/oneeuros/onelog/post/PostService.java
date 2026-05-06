package com.oneeuros.onelog.post;

import com.oneeuros.onelog.board.Board;
import com.oneeuros.onelog.board.BoardRepository;
import com.oneeuros.onelog.comment.Comment;
import com.oneeuros.onelog.comment.CommentService;
import com.oneeuros.onelog.post.dto.PostCreateRequestDto;
import com.oneeuros.onelog.post.dto.PostListResponseDto;
import com.oneeuros.onelog.post.dto.PostUpdateRequestDto;
import com.oneeuros.onelog.post.dto.PostResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
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
    public Post save(PostCreateRequestDto request) {


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
    public PostResponseDto findById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        post.increaseViewCount();

        return new PostResponseDto(
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
    public Page<PostListResponseDto> findPostList(int page){
        Pageable pageable = PageRequest.of(page,4);

//        Page<Post> posts = postRepository.findAllByOrderByCreatedAtDesc(pageable);
        Page<Post> posts = postRepository.findAllWithBoardOrderByCreatedAtDesc(pageable); //fetch join을 이용함.

        // Page<Post>를 Page<PostListResponseDTO>로 변경함
        return posts.map(post -> new PostListResponseDto(
                post.getId(),
                post.getBoard().getId(),
                post.getBoard().getName(),
                post.getTitle(),
                post.getNickname(),
                post.getViewCount(),
                commentService.countCommentsByPostId(post.getId()),
                post.getCreatedAt()
        ));
    }

    // 특정 게시판의 게시글 목록 조회
    @Transactional(readOnly = true)
    public Page<PostListResponseDto> findPostListByBoard(Long boardId, int page) {
        Pageable pageable = PageRequest.of(page, 2);

        Page<Post> posts = postRepository.findAllByBoardIdOrderByCreatedAtDesc(boardId, pageable);

        return posts.map(post -> new PostListResponseDto(
                post.getId(),
                post.getBoard().getId(),
                post.getBoard().getName(),
                post.getTitle(),
                post.getNickname(),
                post.getViewCount(),
                commentService.countCommentsByPostId(post.getId()),
                post.getCreatedAt()
        ));
    }

    // 게시글 수정
    @Transactional
    public Post update(Long postId, PostUpdateRequestDto request){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        if (!PasswordUtils.checkPassword(request.password(), post.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        post.update(request.title(), request.content());

        return post;
    }


}
