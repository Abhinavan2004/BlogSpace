package com.example.demo.controller;

import com.example.demo.domain.entity.Entity_Tags;
import com.example.demo.service.Tag_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/tags")
public class Controller_Tag {

    private final Tag_Service tagService;
}
