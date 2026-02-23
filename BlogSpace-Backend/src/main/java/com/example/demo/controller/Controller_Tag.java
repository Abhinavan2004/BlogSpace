package com.example.demo.controller;

import com.example.demo.domain.dtos.Dto_Tags;
import com.example.demo.domain.entity.Entity_Tags;
import com.example.demo.domain.mappers.TagMapper;
import com.example.demo.service.Tag_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tags")
public class Controller_Tag {

    private final Tag_Service tagService;
    private final TagMapper tagMapper;

    @GetMapping
    public ResponseEntity<List<Dto_Tags>> getAllTags(){
        List<Entity_Tags> tags = tagService.getTags();
        List<Dto_Tags> tagResponses = tags.stream().map(tagMapper::toTagResponse).toList();
        return ResponseEntity.ok(tagResponses);
    }
}
