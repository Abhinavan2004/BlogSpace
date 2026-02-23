package com.example.demo.controller;

import com.example.demo.domain.dtos.Dto_Posts;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts")
public class Controller_Post {

    public ResponseEntity<List<Dto_Posts>> getAllPosts(){

    }
}
