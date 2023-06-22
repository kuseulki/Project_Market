package com.example.project.controller;

import com.example.project.dto.CategoryDto;
import com.example.project.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class CategoryController {

    private final CategoryService service;

    @GetMapping("/categories")
    public String getCategoryForm(Model model) {
        model.addAttribute("categoryDto", new CategoryDto());
        return "category/categoryForm";
    }

    @PostMapping("/categories")
    public String createCategory(@ModelAttribute("categoryDto") CategoryDto request) {
        service.createCategory(request);
        return "redirect:/categories";
    }

    @GetMapping("/categories/list")
    public String getCategories(Model model, @RequestParam(value = "categoryId", required = false) Long categoryId) {
        List<CategoryDto> categories = service.getCategoryList();
        model.addAttribute("categoryList", categories);
        return "category/categoryList";
    }
}