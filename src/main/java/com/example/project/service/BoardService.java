package com.example.project.service;

import com.example.project.dto.BoardDto;
import com.example.project.entity.Board;
import com.example.project.entity.BoardFile;
import com.example.project.entity.Member;
import com.example.project.repository.BoardFileRepository;
import com.example.project.repository.BoardRepository;
import com.example.project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;
    private final MemberRepository memberRepository;
    /**
     * 글 작성
     */
    public void write(BoardDto boardDto, Long memberId) throws IOException {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        if (boardDto.getBoardFile().isEmpty()) {
            Board board = Board.toSaveEntity(boardDto, member);
            boardRepository.save(board);
        } else {

            Board board = Board.toSaveFileEntity(boardDto, member);
            Long savedId = boardRepository.save(board).getId();
            Board boardEntity = boardRepository.findById(savedId).get();

            for (MultipartFile boardFile : boardDto.getBoardFile()) {

                String originalFilename = boardFile.getOriginalFilename();
                String storedFileName = System.currentTimeMillis() + "_" + originalFilename;
                String savePath = "C:/spring_img/" + storedFileName;
                boardFile.transferTo(new File(savePath));
                BoardFile boardFileEntity = BoardFile.toBoardFile(boardEntity, originalFilename, storedFileName);
                boardFileRepository.save(boardFileEntity);
            }
        }
    }

    /**     글 수정  */
    public BoardDto update(BoardDto boardDto, Long memberId) {

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
        Board board = Board.toUpdateBoardEntity(boardDto, member);
        boardRepository.save(board);
        return findById(boardDto.getId());
    }


    /** 글  상세보기   --    댓글  */
    @Transactional
    public BoardDto findById(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("글 상세 보기 실패 : 해당 글을 찾을 수 없음"));
        return BoardDto.toBoardDto(board);
    }

    /**    글 목록  + 검색기능      */
    @Transactional(readOnly = true)
    public Page<BoardDto> list(String searchKeyword, Pageable pageable) {

        int page = pageable.getPageNumber() - 1;
        Pageable pageRequest = PageRequest.of(page, pageable.getPageSize(), pageable.getSort());
        Page<Board> boards;
        if (searchKeyword == null) {
            boards = boardRepository.findAll(pageRequest);
        } else {
            boards = boardRepository.findByTitleContaining(searchKeyword, pageRequest);
        }
        return boards.map(BoardDto::toBoardDto);
    }

    /** 내가 작성한 글 보기 */
    @Transactional(readOnly = true)
    public Page<BoardDto> myBoardAll(Member member, Pageable pageable) {
        Page<Board> boardAll = boardRepository.findAllByMember(member, pageable);
        Page<BoardDto> boardDtoList = boardAll.map(BoardDto::toBoardDto);
        return boardDtoList;
    }

    /**     조회수 증가      */
    @Transactional
    public void updateCount(Long id) {
        boardRepository.updateCount(id);
    }


    /**     글 삭제        */
    public void delete(Long id) {
        boardRepository.deleteById(id);
    }
}