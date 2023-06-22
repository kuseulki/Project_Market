package com.example.project.dto;

import com.example.project.entity.Order;
import com.example.project.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Getter @Setter
public class OrderHistDto {

    private Long orderId;
    private String orderDate;
    private OrderStatus orderStatus;

    private List<OrderItemDto> orderItemDtoList = new ArrayList<>();

    public void addOrderItemDto(OrderItemDto orderItemDto){
        orderItemDtoList.add(orderItemDto);
    }

    public OrderHistDto(Order order){
        this.orderId = order.getId();
        this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
        this.orderStatus = order.getOrderStatus();
    }
}
