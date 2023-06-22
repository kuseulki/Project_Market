package com.example.project.controller;

import com.example.project.dto.WishListDetailDto;
import com.example.project.dto.WishListItemDto;
import com.example.project.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class WishListController {

    private final WishListService wishListService;


    @GetMapping("/wishlist")
    public String wishListHist(Principal principal, Model model){
        List<WishListDetailDto> wishListDetailDtoList = wishListService.getWishList(principal.getName());
        model.addAttribute("wishItems", wishListDetailDtoList);
        return "item/wishList";
    }

    @PostMapping("/wishlist")
    public @ResponseBody ResponseEntity wishList(@RequestBody @Valid WishListItemDto wishListItemDto, BindingResult bindingResult, Principal principal){

        if(bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }
        String userId = principal.getName();
        Long wishItemId;
        try {
            wishItemId = wishListService.addWishList(wishListItemDto, userId);
        } catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Long>(wishItemId, HttpStatus.OK);
    }

    @DeleteMapping("/wishListItem/{wishListItemId}")
    public @ResponseBody ResponseEntity deleteWishListItem(@PathVariable("wishListItemId") Long wishListItemId, Principal principal){

        if(!wishListService.validateWishListItem(wishListItemId, principal.getName())){
            return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        wishListService.deleteWishListItem(wishListItemId);
        return new ResponseEntity<Long>(wishListItemId, HttpStatus.OK);
    }
}