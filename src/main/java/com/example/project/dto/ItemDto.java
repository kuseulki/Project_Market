package com.example.project.dto;

import com.example.project.entity.Category;
import com.example.project.entity.Item;
import com.example.project.entity.ItemImg;
import com.example.project.enums.ItemSellStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ItemDto {

    private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemName;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price;

    @NotBlank(message = "상품 상세는 필수 입력 값입니다.")
    private String itemDetail;

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private Integer stockNumber;

    private ItemSellStatus itemSellStatus;

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();
    private String savePath;

    private Long categoryId;

    private String wishItem;

    private List<Long> itemImgIds = new ArrayList<>();


    // 이미지 관련
    private List<MultipartFile> imgName;
    private List<String> oriImgName;
    private List<String> storedImgName;
    private int imgAttached;

    public Item createItem() {
        Item newItem = new Item();
        newItem.setId(this.getId());
        newItem.setItemName(this.getItemName());
        newItem.setPrice(this.getPrice());
        newItem.setStockNumber(this.getStockNumber());
        newItem.setItemDetail(this.getItemDetail());
        newItem.setItemSellStatus(this.getItemSellStatus());

        // 카테고리 설정
        Category category = new Category();
        category.setId(this.getCategoryId());
        newItem.setCategory(category);
        return newItem;
    }

    public static ItemDto toItemListDto(Item item, String savePath) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setItemName(item.getItemName());
        itemDto.setPrice(item.getPrice());
        itemDto.setItemDetail(item.getItemDetail());
        itemDto.setItemSellStatus(item.getItemSellStatus());
        itemDto.setSavePath(savePath);

        // 카테고리 ID 설정

        itemDto.setCategoryId(item.getCategory().getId());
        return itemDto;
    }

    public static ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setItemName(item.getItemName());
        itemDto.setPrice(item.getPrice());
        itemDto.setStockNumber(item.getStockNumber());
        itemDto.setItemDetail(item.getItemDetail());
        itemDto.setItemSellStatus(item.getItemSellStatus());

        // 카테고리 ID 설정
        itemDto.setCategoryId(item.getCategory().getId());

        if(item.getImgAttached() == 0){
            itemDto.setImgAttached(item.getImgAttached());      // 0
        } else {
            List<String> oriImgNameList = new ArrayList<>();
            List<String> storedImgNameList = new ArrayList<>();
            itemDto.setImgAttached(item.getImgAttached());      // 1

            for(ItemImg itemImg : item.getItemImgList()){
                oriImgNameList.add(itemImg.getOriImgName());
                storedImgNameList.add(itemImg.getStoredImgName());
            }
            itemDto.setOriImgName(oriImgNameList);
            itemDto.setStoredImgName(storedImgNameList);
        }
        return itemDto;
    }
}