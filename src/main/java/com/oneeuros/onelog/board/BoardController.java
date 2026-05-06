package com.oneeuros.onelog.board;

import com.oneeuros.onelog.board.dto.BoardRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;

    //post 방식으로 수정
    //게시판 생성
    @PostMapping ("/create")
    public String saveBoard(
            @Valid BoardRequestDto request,
            BindingResult bindingResult,
            Model model) {
        // 입력값 검증 (컨트롤러 책임)
        if (bindingResult.hasErrors()) {

            if (bindingResult.hasFieldErrors("name")) {
                var errors = bindingResult.getFieldErrors("name");

                for (var error : errors) {
                    String code = error.getCode();

                    if ("NotBlank".equals(code)) {
                        model.addAttribute("errorMessage", "게시판 이름은 필수입니다.");
                    } else if ("Size".equals(code)) {
                        model.addAttribute("errorMessage", "게시판 이름은 30자 이하입니다.");
                    }
                }
            }

            return "boards/boards"; // 추후 연결
        }
        //trim 처리
        String name = request.name().trim();
        Board board = boardService.save(name);
        model.addAttribute("board", board);
        return "boards/boards";
    }

    //게시판 수정
    @PostMapping("/update/{boardId}")
    public String updateBoard(
            @PathVariable Long boardId,
            @RequestParam String name,
            Model model
    ){
        Board board = boardService.update(boardId,name);
        model.addAttribute("board", board);
        return "boards/boards";
    }

    //게시판 삭제
    @PostMapping("/delete/{boardId}")
    @ResponseBody // 테스트를 위해 임의 추가
    public String deleteBoard(
            @PathVariable Long boardId){

        boardService.delete(boardId);
        return "삭제 완료";
        // 추후 메인 페이지로 연결 필요
    }



    // Get 방식
//    @GetMapping ("/create")
//    public String saveBoard(String name, Model model) {
//        if (name == null || name.trim().isEmpty()) {
//            throw new RuntimeException("게시판 이름은 필수입니다");
//        }
//        Board board = boardService.save(name.trim());
//        model.addAttribute("board", board);
//        return "boards/boards";
//    }

}
