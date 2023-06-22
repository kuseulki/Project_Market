package com.example.project.entity;

import com.example.project.dto.ItemDto;
import com.example.project.enums.ItemSellStatus;
import com.example.project.handler.OutOfStockException;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Item extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "item_id")
    private Long id;                        //상품 코드

    @Column(nullable = false, length = 50)
    private String itemName;                //상품명

    @Column(name = "price", nullable = false)
    private int price;                      //가격

    @Column(nullable = false)
    private int stockNumber;                //재고수량

    @Lob
    @Column(nullable = false)
    private String itemDetail;              //상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; //상품 판매 상태

    @Column
    private int imgAttached;               // 상품 이미지 여부, 1,0

    private String wishItem;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Builder.Default
    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ItemImg> itemImgList = new ArrayList<>();


    public void addStock(int stockNumber){
        this.stockNumber += stockNumber;
    }

    public void removeStock(int stockNumber) {
        int restStock = this.stockNumber - stockNumber;
        if (stockNumber < 0) {
            throw new OutOfStockException("상품의 재고가 부족합니다.(현재 재고 수량: " + this.stockNumber + ")");
        }
        this.stockNumber = restStock;
    }

    public static Item updateItem(ItemDto itemDto) {

        Category category = new Category();
        category.setId(itemDto.getCategoryId());

        return Item.builder()
                .id(itemDto.getId())
                .itemName(itemDto.getItemName())
                .price(itemDto.getPrice())
                .stockNumber(itemDto.getStockNumber())
                .itemDetail(itemDto.getItemDetail())
                .itemSellStatus(itemDto.getItemSellStatus())
                .imgAttached(itemDto.getImgAttached() == 0 ? 0 : 1)
                .category(category)
                .build();
    }
}