package com.example.project.entity;

import com.example.project.dto.BoardDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Board extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "board_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    private String content;

    @Column
    private int count;                      // 조회수

    @Column
    private int fileAttached;               // 파일 첨부 여부, 1,0

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member;                  // = boardWrite

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BoardFile> boardFileList = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("id desc")
    private List<Comment> commentList = new ArrayList<>();

    // dto -> entity
    public static Board toSaveEntity(BoardDto boardDto, Member member) {
        Board board = new Board();
        board.setMember(member);
        board.setTitle(boardDto.getTitle());
        board.setContent(boardDto.getContent());
        board.setCount(boardDto.getCount());
        board.setFileAttached(0);   // 파일 x
        return board;
    }

    public static Board toSaveFileEntity(BoardDto boardDto, Member member) {
        Board board = new Board();
        board.setMember(member);
        board.setTitle(boardDto.getTitle());
        board.setContent(boardDto.getContent());
        board.setCount(boardDto.getCount());
        board.setFileAttached(1);   // 파일 o
        return board;
    }

        public static Board toUpdateBoardEntity(BoardDto boardDto, Member member) {
        Board board = new Board();
        board.setId(boardDto.getId());
        board.setMember(member);
        board.setTitle(boardDto.getTitle());
        board.setContent(boardDto.getContent());
        board.setCount(boardDto.getCount());
        return board;
    }





}
