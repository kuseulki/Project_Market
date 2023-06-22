package com.example.project.dto;

import com.example.project.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderItemDto {

    private String itemName;
    private int count;
    private int orderPrice;
    private String savePath;


    public OrderItemDto(OrderItem orderItem, String savePath) {
        this.itemName = orderItem.getItem().getItemName();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.savePath = savePath;
    }
}