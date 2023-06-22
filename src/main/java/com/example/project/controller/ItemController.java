package com.example.project.controller;

import com.example.project.dto.ItemDto;
import com.example.project.entity.Category;
import com.example.project.service.CategoryService;
import com.example.project.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final CategoryService categoryService;


    /**      상품 등록      */
    @GetMapping("/item/new")
    public String itemForm(Model model){
        model.addAttribute("itemDto", new ItemDto());
        model.addAttribute("categoryDto", categoryService.getCategoryList());
        return "item/itemSave";
    }

    @PostMapping("/item/new")
    public String itemSave(@Validated @ModelAttribute ItemDto itemDto, BindingResult bindingResult, Model model,
                           @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, @RequestParam("categoryId") Long categoryId) throws IOException {

        if (bindingResult.hasErrors()) {
            return "item/itemSave";
        }
        if(itemImgFileList.get(0).isEmpty() && itemDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemSave";
        }

        try {
            Category category = categoryService.getCategoryEntity(categoryId);
            itemService.saveItem(itemDto, itemImgFileList);
        } catch (Exception e){
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }
        return "redirect:/";
    }

    /**   상품 수정  */
    @GetMapping("/item/update/{itemId}")
    public String itemUpdateForm(@PathVariable Long itemId, Model model){
        model.addAttribute("categoryDto", categoryService.getCategoryList());

        try {
            ItemDto itemDto = itemService.getItemDtl(itemId);
            model.addAttribute("itemDto", itemDto);
            model.addAttribute("categoryDto", categoryService.getCategoryList());
        } catch(EntityNotFoundException e){
            model.addAttribute("errorMessage", "존재하지 않는 상품 입니다.");
            model.addAttribute("itemDto", new ItemDto());
            return "item/itemUpdate";
        }
        return "item/itemUpdate";
    }

    @PostMapping("/item/update/{itemId}")
    public String itemUpdate(@PathVariable Long itemId, @Validated @ModelAttribute ItemDto itemDto, BindingResult bindingResult,
                             Model model, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList,
                             @RequestParam("categoryId") Long categoryId) throws IOException {

        if (bindingResult.hasErrors()) {
            return "item/itemUpdate";
        }

        if (itemImgFileList.get(0).isEmpty() && itemDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다.");
            model.addAttribute("categoryDto", categoryService.getCategoryList());
            return "item/itemUpdate";
        }

        try {
            Category category = categoryService.getCategoryEntity(categoryId);
            itemService.updateItem(itemId, itemDto, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            model.addAttribute("categoryDto", categoryService.getCategoryList());
            return "item/itemUpdate";
        }
        return "redirect:/";
    }

    @GetMapping("/item/{itemId}")
    public String itemDtl(Model model, @PathVariable("itemId") Long itemId){
        ItemDto itemDto = itemService.getItemDtl(itemId);
        model.addAttribute("item", itemDto);
        return "item/itemDetail";
    }

    /**   상품 목록  + 페이징 + 검색 */
    @GetMapping("/item/list")
    public String home(@PageableDefault(page = 1, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, Model model, String searchKeyword) {

        Page<ItemDto> itemDtoList = itemService.getItemList(searchKeyword, pageable);

        int blockLimit = 5;
        int startPage = (pageable.getPageNumber() - 1) / blockLimit * blockLimit + 1;
        int endPage = Math.min(startPage + blockLimit - 1, itemDtoList.getTotalPages());

        model.addAttribute("items", itemDtoList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return"item/itemList";
    }

    /**  카테고리별 상품 목록  + 페이징 + 검색 */
    @GetMapping("/category/{name}")
    public String getCategoryItems(@PathVariable("name") String name, Model model, @PageableDefault(page = 1, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, String searchKeyword) {

        String categoryName = categoryService.getCategoryName(name);
        Page<ItemDto> itemDtoList = itemService.getItemListByCategory(categoryName, searchKeyword, pageable);

        int blockLimit = 5;
        int startPage = (pageable.getPageNumber() - 1) / blockLimit * blockLimit + 1;
        int endPage = Math.min(startPage + blockLimit - 1, itemDtoList.getTotalPages());

        model.addAttribute("items", itemDtoList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return"item/itemList";
    }

    /**   상품 삭제  */
    @GetMapping("/item/delete/{id}")
    public String deleteItem(@PathVariable Long id){
        itemService.delete(id);
        return "redirect:/item/list";
    }

}
