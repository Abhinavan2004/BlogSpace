package com.example.demo.controller;

import com.example.demo.domain.dtos.Dto_Posts;
import com.example.demo.domain.entity.Entity_Post;
import com.example.demo.domain.entity.Entity_User;
import com.example.demo.domain.mappers.PostMapper;
import com.example.demo.service.Service_Posts;
import com.example.demo.service.Service_User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class Controller_Post {

    private final Service_Posts postService;
    private final PostMapper postMapper;
    private final Service_User serviceuser;
    @GetMapping
    public ResponseEntity<List<Dto_Posts>> getAllPosts(
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) UUID tagId) {
        List<Entity_Post> posts = postService.getAllPosts(categoryId, tagId);
        List<Dto_Posts> postDtos = posts.stream().map(postMapper::toDto).toList();
        return ResponseEntity.ok(postDtos);
    }

    @GetMapping("/drafts")
    public ResponseEntity<List<Dto_Posts>> getAllDrafts(HttpServletRequest request){
        UUID userID = (UUID) request.getAttribute("UserId");
        Entity_User loggedIn = serviceuser.getUserById(userID);
        List<Entity_Post> draftPosts = postService.getDraftsPosts(loggedIn);
        List<Dto_Posts> postDtos = draftPosts.stream().map(postMapper::toDto).toList();
        return ResponseEntity.ok(postDtos);
    }
}

