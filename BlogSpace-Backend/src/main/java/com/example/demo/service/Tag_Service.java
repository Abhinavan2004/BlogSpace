package com.example.demo.service;

import com.example.demo.domain.entity.Entity_Tags;
import com.example.demo.repository.Repository_Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class Tag_Service {
    @Autowired
    private final Repository_Tag repository_tag;

    public List<Entity_Tags> getTags(){
        return repository_tag.findAllWithPostCount();
    }

    public List<Entity_Tags> createTags(Set<String> tagNames){
        List<Entity_Tags> existingTags = repository_tag.findByNameIn(tagNames);
        Set<String> existingNames = existingTags.stream()
                .map(Entity_Tags :: getName)
                .collect(Collectors.toSet());

        List<Entity_Tags> newTags = tagNames.stream()
                .filter(name -> !existingTags.contains(name))
                .map(name -> Entity_Tags.builder()
                        .name(name)
                        .posts(new HashSet<>()).build())
                        .toList();

                List<Entity_Tags> savedTags = new ArrayList<>();
                    if(!newTags.isEmpty()){
                        savedTags = repository_tag.saveAll(newTags);
                    }
                    savedTags.addAll(existingTags);

                    return savedTags ;
                }


        public void delete_Tags(UUID id){
        repository_tag.findById(id).ifPresent(tag ->{
            if ((!tag.getPosts().isEmpty())) {

                throw new IllegalStateException("Cannot Delete tag with posts");
            }
            repository_tag.deleteById(id);
        });
        }

    public Entity_Tags getTagById(UUID tagId) {
        return repository_tag.findById(tagId).orElseThrow(() -> new EntityNotFoundException("Tag Id Not Found")) ;
    }
}

