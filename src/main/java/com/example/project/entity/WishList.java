package com.example.project.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter @Setter
@ToString
public class WishList extends BaseEntity {

    @Id
    @Column(name = "wish_list_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    public static WishList createCart(Member member){
        WishList wishList = new WishList();
        wishList.setMember(member);
        return wishList;
    }

}