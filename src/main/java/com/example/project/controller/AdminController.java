package com.example.project.controller;

import com.example.project.service.ItemService;
import com.example.project.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminController {

    private final ItemService itemService;
    private final MemberService memberService;

    @GetMapping("/admin/page")
    public String adminPage(){
        return "admin/adminPage";
    }

    // 전체 상품 관리
    @GetMapping("/admin/item/all")
    public String itemAll(Model model, Pageable pageable){
        model.addAttribute("items", itemService.allItemView(pageable));
        return "admin/itemAllList";
    }

    // 전체 회원 관리
    @GetMapping("/admin/user/all")
    public String userAll(Model model){
        model.addAttribute("userAll", memberService.findAll());
        return "admin/memberAllList";
    }
}