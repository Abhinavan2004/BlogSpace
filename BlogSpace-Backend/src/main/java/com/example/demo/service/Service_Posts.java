package com.example.demo.service;

import com.example.demo.domain.entity.Entity_Post;
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

        }
    }

}
