package com.example.project.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class WishListItem extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "wish_list_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="wish_list_id")
    private WishList wishList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int count;      // 같은 상품 몇개 담을지

    public static WishListItem createWishListItem(WishList wishList, Item item,  int count) {
        WishListItem wishListItem = new WishListItem();
        wishListItem.setWishList(wishList);
        wishListItem.setItem(item);
        wishListItem.setCount(count);
        return wishListItem;
    }

    public void addCount(int count){
        this.count += count;
    }
}