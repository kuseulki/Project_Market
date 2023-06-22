package com.example.project.repository;

import com.example.project.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListRepository  extends JpaRepository<WishList, Long> {

    WishList findByMemberId(Long memberId);
}
