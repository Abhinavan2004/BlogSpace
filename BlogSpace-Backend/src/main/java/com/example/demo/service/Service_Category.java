package com.example.demo.service;

import com.example.demo.domain.entity.Entity_Category;
import com.example.demo.domain.entity.Entity_Tags;
import com.example.demo.repository.Repository_Category;
import com.example.demo.repository.Repository_Tag;
import jakarta.persistence.EntityNotFoundException;
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
    private final Repository_Tag tagRepository ;

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

    @Transactional
    public Entity_Category updateCategory(UUID id, String newName) {
        Entity_Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));

        if (categoryRepository.existsByName(newName)) {
            throw new IllegalArgumentException("Category already exists with name: " + newName);
        }

        category.setName(newName);
        return categoryRepository.save(category);
    }

    public Optional<Entity_Category> findCategoryByIdOptional(UUID id) {
        return categoryRepository.findById(id); // return Optional directly
    }

    public Optional<Entity_Tags> getTagByIdOptional(UUID id) {
        return tagRepository.findById(id); // return Optional directly
    }

    public Entity_Category findCategoryById(UUID id) {
        return categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Category Id Not Found")) ;
    }



}
