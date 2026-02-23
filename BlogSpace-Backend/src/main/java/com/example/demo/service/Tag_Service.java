package com.example.demo.service;

import com.example.demo.domain.entity.Entity_Tags;
import com.example.demo.repository.Repository_Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

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
    }

