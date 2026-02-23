package com.example.demo.controller;

import com.example.demo.domain.dtos.CreateTag_Request;
import com.example.demo.domain.dtos.Tags_Response;
import com.example.demo.domain.entity.Entity_Tags;
import com.example.demo.domain.mappers.TagMapper;
import com.example.demo.service.Tag_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tags")
public class Controller_Tag {

    @Autowired
    private final Tag_Service tagService;

    private final TagMapper tagMapper;

    @GetMapping
    public ResponseEntity<List<Tags_Response>> getAllTags() {
        List<Entity_Tags> tags = tagService.getTags();
        List<Tags_Response> tagResponses = tags.stream().map(tagMapper::toTagResponse).toList();
        return ResponseEntity.ok(tagResponses);
    }


    @PostMapping
    public ResponseEntity<List<Tags_Response>> createTags(@RequestBody CreateTag_Request createtagrequest) {
        List<Entity_Tags> savedTags = tagService.createTags(createtagrequest.getNames());
        List<Tags_Response> createdTagResponses = savedTags.stream().map(tagMapper::toTagResponse).toList();
        return new ResponseEntity<>(
                createdTagResponses,
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete_Tag(@PathVariable UUID id){
        tagService.delete_Tags(id);
        return ResponseEntity.noContent().build();
    }


}
