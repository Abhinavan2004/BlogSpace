package com.example.demo.service;

import com.example.demo.domain.entity.Entity_Category;
import com.example.demo.repository.Repository_Category;
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

    public Entity_Category createCategory(Entity_Category category) {

    }
}
