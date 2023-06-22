package com.example.project.controller;

import com.example.project.config.auth.PrincipalDetails;
import com.example.project.dto.BoardDto;
import com.example.project.dto.CommentDto;
import com.example.project.service.BoardService;
import com.example.project.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;

    /**   글 작성      */
    @GetMapping("/board/save")
    public String saveForm(){
        return "board/save";
    }

    @PostMapping("/board/save")
    public String save(BoardDto boardDto, @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {
        boardService.write(boardDto, principalDetails.getMember().getId());
        return "redirect:/board/list";
    }

    /**   글 목록   -- 페이징, 검색    */
    @GetMapping("/board/list")
    public String paging(@PageableDefault(page = 1, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, Model model, String searchKeyword){

        Page<BoardDto> boardList = boardService.list(searchKeyword, pageable);

        int blockLimit = 5;
        int startPage = (pageable.getPageNumber() - 1) / blockLimit * blockLimit + 1;
        int endPage = Math.min(startPage + blockLimit - 1, boardList.getTotalPages());

        model.addAttribute("boardList", boardList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "board/list";
    }

    /**   글 상세 보기   -- 댓글 가져오기 */
    @GetMapping("/board/{id}")
    public String findById(@PathVariable Long id, Model model, @PageableDefault(page = 1) Pageable pageable){
        boardService.updateCount(id);
        BoardDto boardDto = boardService.findById(id);

        List<CommentDto> commentDtoList = commentService.findAll(id);
        model.addAttribute("commentList", commentDtoList);
        model.addAttribute("board", boardDto);
        model.addAttribute("page", pageable.getPageNumber());
        return "board/detail";
    }

    /** 글 수정    */
    @GetMapping("/board/update/{id}")
    public String boardUpdateForm(@PathVariable Long id, Model model){
        BoardDto boardDto = boardService.findById(id);
        model.addAttribute("boardUpdate", boardDto);
        return "board/update";
    }

    @PostMapping("/board/update")
    public String boardUpdate(BoardDto boardDto, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails){

        BoardDto board = boardService.update(boardDto, principalDetails.getMember().getId());
        model.addAttribute("board", board);
        return "board/detail";
    }

    /** 글 삭제 */
    @GetMapping("/board/delete/{id}")
    public String deleteBoard(@PathVariable Long id){
        boardService.delete(id);
        return "redirect:/board/paging";
    }

    // 내가 작성글 보기
    @GetMapping("/mypage/myBoard")
    public String index(Model model, @AuthenticationPrincipal PrincipalDetails principalDetail,
                        @PageableDefault(sort="id", direction = Sort.Direction.DESC) Pageable pageable) {

        model.addAttribute("member", principalDetail.getMember());
        model.addAttribute("boards", boardService.myBoardAll(principalDetail.getMember(), pageable));
        return "member/myBoard";
    }
}