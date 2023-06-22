package com.example.project.entity;

import com.example.project.dto.CategoryDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(name = "category_name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    public List<Item> items = new ArrayList<>();

    public static Category toCategoryEntity(CategoryDto categoryDto) {
        Category category = new Category();
        category.setId(categoryDto.getCategoryId());
        category.setName(categoryDto.getCategoryName());
        return category;
    }

    public List<Item> getItems() {
        return items;
    }

    public void addItem(Item item) {
        items.add(item);
        item.setCategory(this);
    }

    public void removeItem(Item item) {
        items.remove(item);
        item.setCategory(null);
    }

    public void changeName(String name) {
        this.name = name;
    }
}
