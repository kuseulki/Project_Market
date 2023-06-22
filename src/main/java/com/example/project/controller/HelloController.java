package com.example.project.controller;

import com.example.project.dto.ItemDto;
import com.example.project.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HelloController {

    private final ItemService itemService;

    /**   글 목록     --- 페이징, 검색 */
    @GetMapping("/")
    public String home(@PageableDefault(page = 1, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, Model model, String searchKeyword) {

        Page<ItemDto> itemDtoList = itemService.getItemList(searchKeyword, pageable);

        int blockLimit = 5;  // 하단에 보여지는 페이지 갯수
        int startPage = (pageable.getPageNumber() - 1) / blockLimit * blockLimit + 1;
        int endPage = Math.min(startPage + blockLimit - 1, itemDtoList.getTotalPages());

        model.addAttribute("items", itemDtoList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "index";
    }
}
