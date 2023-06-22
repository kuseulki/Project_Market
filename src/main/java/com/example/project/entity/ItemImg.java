package com.example.project.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter @Setter
@ToString
public class ItemImg extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="item_img_id")
    private Long id;

    @Column
    private String imgName;             //이미지 파일명

    @Column
    private String oriImgName;          //원본 이미지 파일명

    @Column
    private String storedImgName;

    @Column
    private String savePath;               // 이미지 조회 경로

    @Column
    private String repImgYn;                //대표 이미지 여부


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;


    public void updateItemImg(String oriImgName, String imgName, String savePath){
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.savePath = savePath;
    }
}