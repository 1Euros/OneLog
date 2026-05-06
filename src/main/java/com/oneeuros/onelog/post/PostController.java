package com.oneeuros.onelog.post;

import com.oneeuros.onelog.comment.Comment;
import com.oneeuros.onelog.comment.CommentService;
import com.oneeuros.onelog.post.dto.PostCreateRequestDTO;
import com.oneeuros.onelog.post.dto.PostListResponseDTO;
import com.oneeuros.onelog.post.dto.PostResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;



    // 글 작성 처리
    @PostMapping("/create")
    public String savePost(
            @Valid PostCreateRequestDTO request,
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
        PostResponseDTO response = postService.findById(postId);

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
        Page<PostListResponseDTO> posts = postService.findPostList(page);

        model.addAttribute("posts",posts);

        return "posts/list";
    }

}
