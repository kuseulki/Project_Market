package com.example.project.service;

import com.example.project.dto.CommentDto;
import com.example.project.entity.Board;
import com.example.project.entity.Comment;
import com.example.project.entity.Member;
import com.example.project.repository.BoardRepository;
import com.example.project.repository.CommentRepository;
import com.example.project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public Long save(CommentDto commentDto, Long memberId) {

        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        Optional<Board> optionalBoardEntity = boardRepository.findById(commentDto.getBoardId());

        if (optionalBoardEntity.isPresent()) {
            Board boardEntity = optionalBoardEntity.get();
            Comment commentEntity = Comment.toSaveEntity(commentDto, boardEntity, member);
            return commentRepository.save(commentEntity).getId();
        } else {
            return null;
        }
    }

    /** 댓글 목록 조회 **/
    public List<CommentDto> findAll(Long boardId) {

        Board board = boardRepository.findById(boardId).orElseThrow(() ->
                new IllegalArgumentException("댓글 조회 실패 : 해당 게시물이 존재하지 않습니다."));

        List<Comment> commentList = commentRepository.findAllByBoardOrderByIdDesc(board);
        List<CommentDto> commentDtoList = new ArrayList<>();
        for(Comment comment : commentList){
            CommentDto commentDto = CommentDto.toCommentDto(comment, boardId);
            commentDtoList.add(commentDto);
        }
        return commentDtoList;
    }

    /** 댓글 삭제 **/
    public void delete(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
        commentRepository.delete(comment);
    }
}
