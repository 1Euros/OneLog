package com.oneeuros.onelog.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;


    // 글 작성 처리
    @PostMapping("/create")
    public String savePost(
            PostCreateRequest request,
            Model model
    ){
        Post post = postService.save(request);
        model.addAttribute("post",post);
        return "posts/detail";  // 생성 완료 후 작성된 글로 넘겨야하지만, test를 위해 posts/detail.html 추가
    }


/*    // Get으로 받는 post create test
    @GetMapping("/create")
     public String savePost(String title, String content, String nickname, String password, Model model) {
        Post post = postService.save(title, content, nickname, password);
        model.addAttribute("post", post);
        return "posts/posts";
    }*/
}
