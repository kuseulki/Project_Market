package com.example.project.repository;

import com.example.project.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ItemRepository extends JpaRepository<Item, Long> {


    Page<Item> findByItemNameContaining(String searchKeyword, Pageable pageable);

    Page<Item> findByCategoryIdAndItemNameContaining(Long categoryId, String searchKeyword, Pageable pageable);

    Page<Item> findByCategoryId(Pageable pageable, Long categoryId);
}
