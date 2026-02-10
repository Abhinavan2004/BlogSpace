package com.example.demo.service;

import com.example.demo.domain.entity.Entity_Category;
import com.example.demo.repository.Repository_Category;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class Service_Category {
    private final Repository_Category categoryRepository;

    public List<Entity_Category> listCategories() {
        return categoryRepository.findAllWithPosts();
    }

    @Transactional
    public Entity_Category createCategory(Entity_Category category) {
        if(categoryRepository.categoryexistsbyName(category.getName())){
            throw new IllegalArgumentException("Category Already Present : " + category.getName());
        }
        return  categoryRepository.save(category);
    }
}
