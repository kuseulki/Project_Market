package com.example.project.dto;

import com.example.project.entity.Board;
import com.example.project.entity.BoardFile;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class BoardDto {

    private Long id;
    private String boardWrite;
    private String title;
    private String content;
    private int count;
    private LocalDateTime boardCreatedTime;
    private LocalDateTime boardUpdatedTime;
    private String userId;

    // 파일 관련
    private List<MultipartFile> boardFile;      // save.html -> Controller 파일 담는 용도
    private List<String> originalFileName;      // 원본 파일 이름
    private List<String> storedFileName;        // 서버 저장용 파일 이름
    private int fileAttached;                   // 파일 첨부 여부(첨부: 1, 미첨부: 0)


    public static BoardDto toBoardDto(Board board) {
        BoardDto boardDto = new BoardDto();
        boardDto.setId(board.getId());

        boardDto.setUserId(board.getMember().getUserId());

        boardDto.setBoardWrite(board.getMember().getUserName());
        boardDto.setTitle(board.getTitle());
        boardDto.setContent(board.getContent());
        boardDto.setCount(board.getCount());
        boardDto.setBoardCreatedTime(board.getRegTime());
        boardDto.setBoardUpdatedTime(board.getUpdateTime());

        if(board.getFileAttached() == 0) {
             boardDto.setFileAttached(board.getFileAttached());      // 0
          } else {
            List<String> originalFileNameList = new ArrayList<>();
            List<String> storedFileNameList = new ArrayList<>();
            boardDto.setFileAttached(board.getFileAttached());      // 1

            for(BoardFile boardFile : board.getBoardFileList()){
                originalFileNameList.add(boardFile.getOriginalFileName());
                storedFileNameList.add(boardFile.getStoredFileName());
            }
            boardDto.setOriginalFileName(originalFileNameList);
            boardDto.setStoredFileName(storedFileNameList);
          }
          return boardDto;
      }
    }