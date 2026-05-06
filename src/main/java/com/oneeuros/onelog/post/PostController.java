package com.oneeuros.onelog.post;

import com.oneeuros.onelog.comment.Comment;
import com.oneeuros.onelog.comment.CommentService;
import com.oneeuros.onelog.post.dto.PostCreateRequestDTO;
import com.oneeuros.onelog.post.dto.PostResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final CommentService commentService;



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

    // 게시판 상세 조회
    @GetMapping("/{postId}")
    public String detailPost(@PathVariable Long postId, Model model){
        PostResponseDTO response = postService.findById(postId);

        // 대표 댓글 한개 가져옴
        Comment representativeComments = commentService.findFirstComment(postId);

        // 게시글의 댓글 수 조회
        int commentCount = commentService.countCommentsByPostId(postId);


//        // 최신순으로 comment 가져옴
//        List<Comment> comments =
//                commentService.findComments(postId);
//
//        List<Comment> representativeComments = comments.stream().limit(1).toList();

        model.addAttribute("post",response.post());
        model.addAttribute("representativeComments",representativeComments);
        model.addAttribute("commentCount",commentCount);

        return "posts/detail";
    }

}
