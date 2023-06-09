package com.sparta.board.service;

import com.sparta.board.dto.BoardRequestDto;
import com.sparta.board.dto.BoardResponseDto;
import com.sparta.board.entity.Board;
import com.sparta.board.entity.BoardLike;
import com.sparta.board.entity.User;
import com.sparta.board.entity.UserRoleEnum;
import com.sparta.board.exception.CustomException;
import com.sparta.board.exception.StatusCode;
import com.sparta.board.repository.BoardLikeRepository;
import com.sparta.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.sparta.board.exception.StatusCode.PAGE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardLikeRepository boardLikeRepository;

    @Transactional(readOnly = true)
    public List<BoardResponseDto> getBoards(Pageable pageable) {
        Page<Board> boards = boardRepository.findAll(pageable);
        if(boards.isEmpty() && pageable.getPageNumber() > 0) {  //끝페이지에 대한 예외 처리
            throw new CustomException(PAGE_NOT_FOUND); //(끝페이지가 size의 배수가 아닐 경우 표시되지 않으므로.)
        }
        return boards.stream()
                .map(BoardResponseDto::new)
                .collect(Collectors.toList());
    }

    public BoardResponseDto getBoard(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new CustomException(StatusCode.USER_NOT_FOUND)
        );
        return new BoardResponseDto(board);
    }

    // 글 작성
    @Transactional
    public BoardResponseDto createBoard(BoardRequestDto boardRequestDto) {
        Board board = boardRepository.saveAndFlush(new Board(boardRequestDto));
        return new BoardResponseDto(board);
    }

    @Transactional
    public BoardResponseDto updateBoard(Long id, BoardRequestDto requestDto, User user) {
        //사용자 권한 가져와서 ADMIN이면 모든 게시글 수정 가능
        UserRoleEnum userRoleEnum = user.getRole();

        if (userRoleEnum == UserRoleEnum.USER) {
            Board board = boardRepository.findByIdAndCreatedBy(id, user.getUsername()).orElseThrow(
                    () -> new CustomException(StatusCode.INVALID_USER)
            );
            board.updateBoard(requestDto);
            return new BoardResponseDto(board);
        } else {
            Board board = boardRepository.findById(id).orElseThrow(
                    () -> new CustomException(StatusCode.BOARD_NOT_FOUND)
            );
            board.updateBoard(requestDto);
            return new BoardResponseDto(board);
        }
    }

    @Transactional
    public void deleteBoard(Long id, User user) {
        UserRoleEnum userRoleEnum = user.getRole();

        if (userRoleEnum == UserRoleEnum.USER) {
            Board board = boardRepository.findByIdAndCreatedBy(id, user.getUsername()).orElseThrow(
                    () -> new CustomException(StatusCode.INVALID_USER)
            );
            boardRepository.delete(board);
        } else {
            Board board = boardRepository.findById(id).orElseThrow(
                    () -> new CustomException(StatusCode.BOARD_NOT_FOUND)
            );
            boardRepository.delete(board);
        }
    }

    @Transactional
    public boolean likeBoard(Long id, User user) {
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new CustomException(StatusCode.BOARD_NOT_FOUND)
        );

        //first 좋아요 처리 : 좋아요 null이면 boardlike 생성 후 좋아요 처리
        if (boardLikeRepository.findByBoardAndUser(board, user) == null) {
            board.setLikeCount(board.getLikeCount() + 1);
            BoardLike boardLike = new BoardLike(board, user);
            boardLikeRepository.save(boardLike);
            return boardLike.isStatus();
        } else {
            //좋아요 누른 적 있으면 : 취소 후 테이블 삭제
            BoardLike boardLike = boardLikeRepository.findByBoardAndUser(board, user);
            boardLike.unLikeBoard(board);
            boardLikeRepository.delete(boardLike);
            return boardLike.isStatus();
        }
    }
}
