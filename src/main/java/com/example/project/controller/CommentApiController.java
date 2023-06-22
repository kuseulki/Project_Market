package com.example.project.controller;

import com.example.project.config.auth.PrincipalDetails;
import com.example.project.dto.CommentDto;
import com.example.project.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class  CommentApiController {

    private final CommentService commentService;

    /** 댓글 작성 **/
    @PostMapping("/comment/save")
    public ResponseEntity save(@ModelAttribute CommentDto commentDto, @AuthenticationPrincipal PrincipalDetails user) {

        Long saveResult = commentService.save(commentDto, user.getMember().getId());
        
        if (saveResult != null) {
            List<CommentDto> commentDTOList = commentService.findAll(commentDto.getBoardId());
            return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("해당 게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }
    }

    /** 댓글 삭제 **/
    @DeleteMapping("/board/{boardId}/comment/{commentId}")
    public ResponseEntity delete(@PathVariable("commentId") Long commentId){
        commentService.delete(commentId);
        return new ResponseEntity(HttpStatus.OK);
    }
}