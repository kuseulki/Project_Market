package com.example.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class WishListDetailDto {

    private Long wishListItemId;
    private String itemName;
    private int price;
    private int count;
    private String savePath;

    public WishListDetailDto(Long wishListItemId, String itemName, int price, int count, String savePath){
        this.wishListItemId = wishListItemId;
        this.itemName = itemName;
        this.price = price;
        this.count = count;
        this.savePath = savePath;
    }

}