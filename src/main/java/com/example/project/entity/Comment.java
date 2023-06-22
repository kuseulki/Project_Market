package com.example.project.entity;

import com.example.project.dto.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "comment_id")
    private Long id;

    @Column(length = 20, nullable = false)
    private String commentWriter;

    @Column
    private String commentContents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static Comment toSaveEntity(CommentDto commentDto, Board board, Member member) {
        Comment comment = new Comment();
        comment.setMember(member);
        comment.setCommentWriter(commentDto.getCommentWriter());
        comment.setCommentContents(commentDto.getCommentContents());
        comment.setBoard(board);
        return comment;
    }
}
