package com.sparta.soloproject.controller;

import com.sparta.soloproject.dto.BoardRequestDto;
import com.sparta.soloproject.dto.BoardResponseDto;
import com.sparta.soloproject.entity.Board;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class BoardController {

    private final Map<Long, Board> boardList = new HashMap<>();

    @PostMapping("/boards")
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto requestDto) {
        // RequestDto -> Entity
        Board board = new Board(requestDto);

        // Board Max ID Check
        Long maxId = boardList.size() > 0 ? Collections.max(boardList.keySet()) + 1 : 1;
        board.setId(maxId);

        // DB 저장
        boardList.put(board.getId(), board);

        // Entity -> ResponseDto
        BoardResponseDto boardResponseDto = new BoardResponseDto(board);

        return boardResponseDto;
    }

    @GetMapping("/boards")
    public List<BoardResponseDto> getBoards() {
        // Map To List
        List<BoardResponseDto> responseList = boardList.values().stream()
                .map(BoardResponseDto::new).toList();
        return responseList;
    }

    //업데이트
    @PutMapping("/boards/{id}")
    public Long updateBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto) {
        //해당 게시판이 DB에 존재하는지 확인
        if (boardList.containsKey(id)) {
            //해당 게시판 가져오기
            Board board = boardList.get(id);

            // 게시판 수정
            board.update(requestDto);
            return board.getId();
        } else {
            throw new IllegalArgumentException("선택한 게시판은 존재하지 않습니다.");
        }
    }

    @DeleteMapping("/board/{id}")
    public Long deleteBoard(@PathVariable Long id) {
        //해당 게시판이 DB에 존재하는지 확인
        if (boardList.containsKey(id)) {
            //해당 게시판 삭제
            boardList.remove(id);
            return id;
        } else {
            throw new IllegalArgumentException("선택한 게시판은 존재하지 않습니다.");
        }
      }
}