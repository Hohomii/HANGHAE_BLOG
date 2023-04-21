package com.sparta.board.Service;

import com.sparta.board.dto.BoardResponseDto;
import com.sparta.board.entity.Board;
import com.sparta.board.repository.BoardRepository;
import com.sparta.board.dto.BoardRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // 생성자 주입 위한 어노테이션
public class BoardService {

    private final BoardRepository boardRepository; // 생성자 주입

    // 글 전체 조회. Jpa 쿼리 메서드 사용해서 작성일 기준 내림차순. 반환값 리스트
    @Transactional(readOnly = true)
    public List<BoardResponseDto> getBoards() {
        return boardRepository.findByOrderByModifiedAtDesc().stream()
                .map(b -> new BoardResponseDto(b))
                .collect(Collectors.toList()); // 이 부분 공부 필요
    }

    // 글 작성. requestDto 담아서 repo에 저장 요청. 반환값 entity
    @Transactional
    public BoardResponseDto createBoard(BoardRequestDto requestDto) {
        // 브라우저에서 받아온 데이터를 저장하기 위해서 Blog 객체로 변환
        Board board = new Board(requestDto);
        boardRepository.save(board);
        return new BoardResponseDto(board);
    }

    // 선택한 글 조회
    public BoardResponseDto getBoard(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        return new BoardResponseDto(board);
    }

    // 글 수정. id와 requestDto 담아서 entity의 update메서드 호출.
    // id 존재하는지 확인 후 없을 경우의 예외 처리
    // requestDto로 넘어온 비밀번호가 해당 id의 비밀번호와 일치하는지 확인 후 일치할 경우에만 update 후 메시지 반환
    // 일치하지 않으면 업데이트 없이 실패 메시지 반환
    @Transactional
    public BoardResponseDto updateBoard(Long id, BoardRequestDto requestDto) {
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
//
//        if (board.getPassword().equals(requestDto.getPassword())) {
//            board.updateBoard(requestDto);
//            return new BoardResponseDto(board);
//        } else {
//            return null;
//        }
//    }

    // 글 삭제. 로직은 글 수정하는 부분과 동일
    @Transactional
    public String deleteBoard(Long id, BoardRequestDto requestDto) {
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );

        if (board.getPassword().equals(requestDto.getPassword())) {
            boardRepository.deleteById(id);
            return "글 삭제 성공!";
        } else {
            return "글 삭제 실패!";
        }
    }

}
