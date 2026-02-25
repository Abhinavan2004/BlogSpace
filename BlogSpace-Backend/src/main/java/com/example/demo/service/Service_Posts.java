package com.example.demo.service;

import com.example.demo.domain.CreatePostRequest;
import com.example.demo.domain.Enum_Post;
import com.example.demo.domain.UpdatePostRequest;
import com.example.demo.domain.dtos.Update_Post_Dto;
import com.example.demo.domain.entity.Entity_Category;
import com.example.demo.domain.entity.Entity_Post;
import com.example.demo.domain.entity.Entity_Tags;
import com.example.demo.domain.entity.Entity_User;
import com.example.demo.repository.Repository_Post;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Service_Posts {

    private static final double WORDS_PER_MINUTE = 200;
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

    public List<Entity_Post> getDraftsPosts(Entity_User user){
        return repository_post.findAllByAuthorAndStatus(user ,  Enum_Post.DRAFT);
    }

    public Entity_Post createPost(Entity_User user , CreatePostRequest cpr){
        Entity_Post post = new Entity_Post();
        post.setTitle(cpr.getTitle());
        post.setContent(cpr.getContent());
        post.setStatus(cpr.getStatus());
        post.setAuthor(user);
        post.setReadingTime(calculateReadingTime((cpr.getContent())));
        Entity_Category category = service_category.findCategoryById(cpr.getCategoryId());
        post.setCategory(category);

        Set<UUID> tagIDs = cpr.getTagIds();
        List<Entity_Tags> tags = tagservice.getTagsByIds(tagIDs);
        post.setTags(new HashSet<>(tags));

        return repository_post.save(post);
    }

    private Integer calculateReadingTime(String content){
        if(content == null ||  content.isEmpty()){
            return 0;
        }
        int wordCount = content.trim().split("\\s+").length;
        return (int) Math.ceil((double) wordCount / WORDS_PER_MINUTE);
    }

    @Transactional
    public Entity_Post updatePost(UUID id , UpdatePostRequest updatePostRequest){
        Entity_Post existingPost = repository_post.findById(id).
                orElseThrow(() ->  new EntityNotFoundException("Post does not exist with id : " + id));

        existingPost.setTitle(updatePostRequest.getTitle());
        String postContent = updatePostRequest.getContent() ;
        existingPost.setContent(postContent);
        existingPost.setStatus(updatePostRequest.getStatus());
        existingPost.setReadingTime(calculateReadingTime(postContent));

        UUID updatePostRequestCategoryId = updatePostRequest.getCategoryId();
        if(!existingPost.getCategory().getId().equals(updatePostRequestCategoryId)){
            Entity_Category newcategory = service_category.findCategoryById(updatePostRequestCategoryId);
            existingPost.setCategory(newcategory);
        }
       Set<UUID> existingTagIds = existingPost.getTags().stream().map(Entity_Tags::getId).collect(Collectors.toSet());
        Set<UUID> updatePostRequestTagIds = updatePostRequest.getTagIds();
        if(!existingTagIds.equals(updatePostRequestTagIds)){
            List<Entity_Tags> newTags = tagservice.getTagsByIds(updatePostRequestTagIds);
            existingPost.setTags(new HashSet<>(newTags));
        }
        return repository_post.save(existingPost);
    }

    public Entity_Post getPost(UUID id){
        return  repository_post.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post does not exist with id : " + id));
    }

    public void deletePost(UUID id){
        repository_post.delete(getPost(id));
    }
}
