package com.example.project.dto;

import com.example.project.entity.Comment;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommentDto {

    private Long id;
    private String userId;
    private String commentWriter;
    private String commentContents;
    private Long boardId;
    private LocalDateTime commentCreatedTime;

    public static CommentDto toCommentDto(Comment comment, Long boardId) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setUserId(comment.getMember().getUserId());
        commentDto.setCommentWriter(comment.getMember().getUserName());
        commentDto.setCommentContents(comment.getCommentContents());
        commentDto.setCommentCreatedTime(comment.getRegTime());
        commentDto.setBoardId(boardId);
        return commentDto;
    }
}