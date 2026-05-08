package com.oneeuros.onelog.post;

import com.oneeuros.onelog.board.Board;
import com.oneeuros.onelog.board.BoardService;
import com.oneeuros.onelog.post.dto.PostCreateRequestDto;
import com.oneeuros.onelog.post.dto.PostListResponseDto;
import com.oneeuros.onelog.post.dto.PostResponseDto;
import com.oneeuros.onelog.post.dto.PostUpdateRequestDto;
import org.springframework.data.domain.Page;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final BoardService boardService;

    // 게시글 생성 화면
    @GetMapping("/create")
    public String createPostForm(
            @RequestParam(required = false) Long boardId,
            Model model
    ){
        List<Board> boards = boardService.findAllBoards();

        model.addAttribute("boards", boards);
        model.addAttribute("selectedBoardId", boardId);

        return "posts/create";
    }


    // 글 작성 처리
    @PostMapping("/create")
    public String savePost(
            @Valid PostCreateRequestDto request,
            BindingResult bindingResult,
            Model model
    ){
        // 유효성 검증
        if (bindingResult.hasErrors()) {
            // 화면 출력용
            model.addAttribute("errorMessage", bindingResult.getAllErrors().get(0).getDefaultMessage());
            // 콘솔 출력용
            System.out.println("errorMessage = " + bindingResult.getAllErrors().get(0).getDefaultMessage());
            return "posts/errortest"; // 임시 테스트용
        }

        Post post = postService.save(request);
        model.addAttribute("post",post);
        //return "posts/detail";  // 생성 완료 후 작성된 글로 넘겨야하지만, test를 위해 posts/detail.html 추가
        return "redirect:/post/" + post.getId();
    }

    // 게시글 상세 조회
    @GetMapping("/{postId}")
    public String detailPost(@PathVariable Long postId, Model model){
        PostResponseDto response = postService.findById(postId);

        model.addAttribute("post",response.post());
        model.addAttribute("representativeComments",response.comment());
        model.addAttribute("commentCount",response.commentCount());

        return "posts/detail";
    }

    // 게시글 전체 조회
    @GetMapping
    public String postList(
            @RequestParam(defaultValue = "0") int page,
            Model model
    ){
        Page<PostListResponseDto> posts = postService.findPostList(page);

        model.addAttribute("posts",posts);

        return "posts/list";
    }

    // 특정 게시판 게시글 조회
    @GetMapping("/board/{boardId}/posts")
    public String getPostsByBoard(
            @PathVariable Long boardId,
            @RequestParam(defaultValue = "0") int page,
            Model model
    ) {
        Page<PostListResponseDto> posts = postService.findPostListByBoard(boardId, page);
        Board board = boardService.findById(boardId);

        model.addAttribute("posts", posts);
        model.addAttribute("board", board);

        return "posts/list";
    }

    // 게시글 수정
    @PostMapping("/update/{postId}")
    public String updatePost(
            @PathVariable Long postId,
            @Valid PostUpdateRequestDto request,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", bindingResult.getAllErrors().get(0).getDefaultMessage());
            model.addAttribute("post", postService.findPostForEdit(postId));
            model.addAttribute("boards", boardService.findAllBoards());
            return "posts/edit-post :: editPostSection";
        }

        try {
            Post post = postService.update(postId, request);
            return "redirect:/post/" + post.getId();
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("post", postService.findPostForEdit(postId));
            model.addAttribute("boards", boardService.findAllBoards());
            return "posts/edit-post :: editPostSection";
        }
    }




}
