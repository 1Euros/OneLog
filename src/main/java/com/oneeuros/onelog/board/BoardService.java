package com.oneeuros.onelog.board;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    @Transactional
    public Board save(String name){
        if (boardRepository.existsByName(name)) { //S
            throw new IllegalArgumentException("이미 존재하는 게시판입니다");
        }
        Board board = new Board(name);
        return boardRepository.save(board);
    }

    @Transactional
    public Board update(Long boardId, String name){
        // 게시판 존재 여부 검증
        Board board = boardRepository.findById(boardId)   //S
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시판입니다"));

        // 기본 게시판 수정 금지
        if (boardId == 1L) {  //S
            throw new IllegalArgumentException("기본 게시판은 수정할 수 없습니다");
        }

        // 이름 검증
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("게시판 이름을 입력하세요");
        }
        name = name.trim();

        // 이름 중복 체크
        if (boardRepository.existsByName(name)) { //S
            throw new IllegalArgumentException("이미 존재하는 게시판입니다");
        }

        // 엔티티 변경
        board.updateName(name);

        return board;
    }

    @Transactional
    public void delete(Long boardId){
        // 게시판 존재 여부 검증
        Board board = boardRepository.findByIdWithPosts(boardId)  //S
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시판입니다"));

        // 기본 게시판 삭제 금지
        if (boardId == 1L) {
            throw new IllegalArgumentException("기본 게시판은 삭제할 수 없습니다");
        }

        // 게시글 존재 여부 확인
        if(!board.getPostlist().isEmpty()){  //S
            throw new IllegalArgumentException("게시글이 존재하는 게시판은 삭제할 수 없습니다");
        }

        // 삭제
        boardRepository.delete(board);
    }
}
