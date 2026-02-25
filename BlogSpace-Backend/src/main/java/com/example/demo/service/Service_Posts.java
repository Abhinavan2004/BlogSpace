package com.example.demo.service;

import com.example.demo.domain.CreatePostRequest;
import com.example.demo.domain.Enum_Post;
import com.example.demo.domain.entity.Entity_Category;
import com.example.demo.domain.entity.Entity_Post;
import com.example.demo.domain.entity.Entity_Tags;
import com.example.demo.domain.entity.Entity_User;
import com.example.demo.repository.Repository_Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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
        return repository_post.findAllByAuthorAndStatus(user ,  Enum_Post.PUBLISHED);
    }

    public Entity_Post createPost(Entity_User user , CreatePostRequest cpr){
        Entity_Post post = new Entity_Post();
        post.setTitle(cpr.getTitle());
        post.setContent(cpr.getContent());
        post.setStatus(cpr.getStatus());
        post.setAuthor(user);
        post.setReadingTime(calculateReadingTime((cpr.getContent())));
        Entity_Category category = service_category.findCategoryById(cpr.getCategoryID());
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
}
