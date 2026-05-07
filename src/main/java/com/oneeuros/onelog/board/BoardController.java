package com.oneeuros.onelog.board;

import com.oneeuros.onelog.board.dto.BoardRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;

    // 게시판 생성 화면 열기
    @GetMapping("/create-open")
    public String openCreateForm(Model model) {
        model.addAttribute("boardRequestDto", new BoardRequestDto(""));
        return "boards/create-form";
    }

    // 게시판 생성 화면 fragment 반환 (모달용)
    @GetMapping("/create-open/fragment")
    public String openCreateFormFragment(Model model) {
        model.addAttribute("boardRequestDto", new BoardRequestDto(""));
        return "boards/create-form :: createForm";
    }

    // 게시판 수정 화면 열기
    @GetMapping("/update-open/{boardId}")
    public String openUpdateForm(@PathVariable Long boardId, Model model, RedirectAttributes redirectAttributes){
        try{
            //서비스 호출 (존재하지 않으면 IllegalArgumentException 발생)
            Board board = boardService.findById(boardId);

            // 조회 성공 시 모델에 담기
            model.addAttribute("boardRequestDto", new BoardRequestDto(board.getName()));
            model.addAttribute("boardId", boardId);

            return "boards/update-form";

        } catch (IllegalArgumentException e) {
        // 서비스에서 던진 "존재하지 않는 게시판입니다" 메시지를 받아서 리다이렉트
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/"; //메인으로 이동 (추후 수정)
        }
    }

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
            model.addAttribute("boardRequestDto", new BoardRequestDto(""));
            return "boards/create-form :: createForm";
        } else if (boardService.existBoardName(request.name())) {
            model.addAttribute("errorMessage", "이미 존재하는 게시판입니다");
            model.addAttribute("boardRequestDto", new BoardRequestDto(""));
            return "boards/create-form :: createForm";
        }
        //trim 처리
        String name = request.name().trim();
        Board board = boardService.save(name);
        model.addAttribute("board", board);
        return "redirect:/post/board/%s/posts".formatted(board.getId());
    }

    //게시판 수정
    @PostMapping("/update/{boardId}")
    public String updateBoard(
            @PathVariable Long boardId,
            @Valid BoardRequestDto request,
            BindingResult bindingResult,
            Model model
    ){
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
            Board board = boardService.findById(boardId);
            model.addAttribute("boardRequestDto", new BoardRequestDto(board.getName()));
            model.addAttribute("boardId", boardId);
            return "boards/update-form";// 기존 창으로 다시 돌아가기
        }else if (boardService.existBoardName(request.name())) {
            model.addAttribute("errorMessage", "이미 존재하는 게시판입니다");
            Board board = boardService.findById(boardId);
            model.addAttribute("boardRequestDto", new BoardRequestDto(board.getName()));
            model.addAttribute("boardId", boardId);
            return "boards/update-form";
        }

        //trim 처리
        String name = request.name().trim();

        Board board = boardService.update(boardId,name);

        model.addAttribute("board", board);
        return "redirect:/post/board/%s/posts".formatted(board.getId());
    }

    //게시판 삭제
    @PostMapping("/delete/{boardId}")
    public String deleteBoard(
            @PathVariable Long boardId, Model model){

        try {
            boardService.delete(boardId);
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage",e.getMessage());
            Board board = boardService.findById(boardId);
            model.addAttribute("boardRequestDto", new BoardRequestDto(board.getName()));
            model.addAttribute("boardId", boardId);
            return "boards/update-form";
        }
        return "redirect:/post";
        // 추후 메인 페이지로 연결 필요
    }

    // 게시판 목록 조회
    @GetMapping("/boards")
    public String getBoards(Model model){
        List<Board> boards = boardService.findAllBoards();

        if (boards.isEmpty()){
            model.addAttribute("errorMessage", "게시판 목록을 불러올 수 없습니다");
        }
        model.addAttribute("boards",boards);
        return "boards/list";
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
