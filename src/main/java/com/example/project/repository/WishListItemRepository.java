package com.example.project.repository;

import com.example.project.dto.WishListDetailDto;
import com.example.project.entity.WishListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WishListItemRepository extends JpaRepository<WishListItem, Long> {

    WishListItem findByWishListIdAndItemId(Long wishListId, Long itemId);

    @Query("select new com.example.project.dto.WishListDetailDto(wi.id, i.itemName, i.price, wi.count, im.savePath) " +
            "from WishListItem wi, ItemImg im " +
            "join wi.item i " +
            "where wi.wishList.id = :wishListId " +
            "and im.item.id = wi.item.id " +
            "and im.repImgYn = 'Y' " +
            "order by wi.regTime desc"
    )
    List<WishListDetailDto> findWishListDetailDtoList(Long wishListId);
}
