package com.example.project.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class BoardFile extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "board_file_id")
    private Long id;

    @Column
    private String originalFileName;

    @Column
    private String storedFileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    public static BoardFile toBoardFile(Board board, String originalFileName, String storedFileName) {
        BoardFile boardFile = new BoardFile();
        boardFile.setOriginalFileName(originalFileName);
        boardFile.setStoredFileName(storedFileName);
        boardFile.setBoard(board);
        return boardFile;
    }
}