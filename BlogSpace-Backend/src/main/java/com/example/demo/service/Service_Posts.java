package com.example.demo.service;

import com.example.demo.domain.Enum_Post;
import com.example.demo.domain.entity.Entity_Category;
import com.example.demo.domain.entity.Entity_Post;
import com.example.demo.domain.entity.Entity_Tags;
import com.example.demo.repository.Repository_Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class Service_Posts {

    private final Tag_Service tagservice;
    private final Service_Category service_category;
    private final Repository_Post  repository_post;

    public List<Entity_Post> getAllPosts(UUID tagId , UUID categoryId){
        if(categoryId != null && tagId != null ){
            Entity_Category category = service_category.findCategoryById(categoryId);
            Entity_Tags tag = tagservice.getTagById(tagId);
            return repository_post.findAllByStatusAndCategoryAndTags(
                    Enum_Post.PUBLISHED,
                    category,
                    tag
            );
        }
        if(categoryId != null){
            Entity_Category category = service_category.findCategoryById(categoryId);
            return repository_post.findAllByStatusAndCategory(
                    Enum_Post.PUBLISHED,
                    category
            );
        }
        if(tagId != null ){
            Entity_Tags tag = tagservice.getTagById(tagId);
            return repository_post.findAllByStatusAndTagsContaining(
                    Enum_Post.PUBLISHED,
                    tag
            );
        }
        return repository_post.findAllByStatus(Enum_Post.PUBLISHED);
    }

}
