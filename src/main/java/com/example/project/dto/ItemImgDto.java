package com.example.project.dto;

import com.example.project.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemImgDto {

    private Long id;
    private String imgName;
    private String oriImgName;
    private String savePath;
    private String repImgYn;

    public static ItemImgDto toItemImgDto(ItemImg itemImg) {
        ItemImgDto itemImgDto = new ItemImgDto();
        itemImgDto.setId(itemImg.getId());
        itemImgDto.setImgName(itemImg.getImgName());
        itemImgDto.setOriImgName(itemImg.getOriImgName());
        itemImgDto.setSavePath(itemImg.getSavePath());
        itemImgDto.setRepImgYn(itemImg.getRepImgYn());
        return itemImgDto;
    }

}
