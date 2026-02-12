package com.example.demo.service;

import com.example.demo.domain.entity.Entity_Category;
import com.example.demo.repository.Repository_Category;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class Service_Category {
    private final Repository_Category categoryRepository;

    public List<Entity_Category> listCategories() {
        return categoryRepository.findAllWithPosts();
    }

    @Transactional
    public Entity_Category createCategory(Entity_Category category) {
        if(categoryRepository.existsByName(category.getName())){
            throw new IllegalArgumentException("Category Already Present : " + category.getName());
        }
        return  categoryRepository.save(category);
    }


    public void delete_the_category(UUID id) {
       Optional<Entity_Category> categoryyy =  categoryRepository.findById(id);
        if(categoryyy.isPresent()){
            if(!categoryyy.get().getPosts().isEmpty()){
                throw new  IllegalStateException("Category contains posts related to it !!!");
            }
            categoryRepository.delete(categoryyy.get());

        }
    }
}
