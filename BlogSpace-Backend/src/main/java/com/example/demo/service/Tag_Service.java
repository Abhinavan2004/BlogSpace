package com.example.demo.service;

import com.example.demo.domain.entity.Entity_Tags;
import com.example.demo.repository.Repository_Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class Tag_Service {
    @Autowired
    private final Repository_Tag repository_tag;

    public List<Entity_Tags> getTags(){
        return repository_tag.findAllWithPostCount();
    }
}
