package com.example.project.dto;

import com.example.project.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    private Long categoryId;
    private String categoryName;

    public static CategoryDto toCategoryDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setCategoryId(category.getId());
        categoryDto.setCategoryName(category.getName());
        return categoryDto;
    }
}
