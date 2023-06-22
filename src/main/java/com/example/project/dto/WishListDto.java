package com.example.project.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WishListDto {

    private Long wishListItemId;
    private List<WishListDto> cartOrderDtoList;

}
