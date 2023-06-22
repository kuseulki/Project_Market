package com.example.project.service;

import com.example.project.dto.CategoryDto;
import com.example.project.entity.Category;
import com.example.project.entity.Item;
import com.example.project.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**     카테고리 등록     */
    @Transactional
    public Long createCategory(CategoryDto categoryDto) {
        Category category = Category.toCategoryEntity(categoryDto);
        categoryRepository.save(category);
        return category.getId();
    }


    /**     상품별 목록 조회      */
    public List<Item> findItemsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 카테고리 ID=" + categoryId));
        return category.getItems();
    }

    /**     카테고리 목록 조회      */
    @Transactional(readOnly = true)
    public List<CategoryDto> getCategoryList() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(CategoryDto::toCategoryDto)
                .collect(Collectors.toList());
    }

    /**     카테고리 삭제     */
    @Transactional
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + categoryId));
        categoryRepository.delete(category);
    }

    /**     카테고리 ID로 카테고리 엔티티 조회    */
    public Category getCategoryEntity(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 카테고리 ID=" + categoryId));
    }

        public String getCategoryName(String name) {
        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 카테고리 이름=" + name));
        return category.getName();
    }
}