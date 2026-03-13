package com.example.demo.controller;

import com.example.demo.domain.CreatePostRequest;
import com.example.demo.domain.UpdatePostRequest;
import com.example.demo.domain.dtos.Create_Post_Dto;
import com.example.demo.domain.dtos.Dto_Posts;
import com.example.demo.domain.dtos.Update_Post_Dto;
import com.example.demo.domain.entity.Entity_Post;
import com.example.demo.domain.entity.Entity_User;
import com.example.demo.domain.mappers.PostMapper;
import com.example.demo.repository.Repository_User;
import com.example.demo.service.Service_Posts;
import com.example.demo.service.Service_User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class Controller_Post {

    private final Service_Posts postService;
    private final PostMapper postMapper;
    private final Service_User serviceuser;
    private final Repository_User repository_user;

    // Helper to get Entity_User from the JWT principal (works for Google + email login)
    private Entity_User getLoggedInUser(UserDetails userDetails) {
        return repository_user.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found in DB"));
    }

    @GetMapping
    public ResponseEntity<List<Dto_Posts>> getAllPosts(
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) UUID tagId) {
        List<Entity_Post> posts = postService.getAllPosts(categoryId, tagId);
        List<Dto_Posts> postDtos = posts.stream().map(postMapper::toDto).toList();
        return ResponseEntity.ok(postDtos);
    }

    @GetMapping("/drafts")
    public ResponseEntity<List<Dto_Posts>> getAllDrafts(
            @AuthenticationPrincipal UserDetails userDetails) {
        Entity_User loggedIn = getLoggedInUser(userDetails);
        List<Entity_Post> draftPosts = postService.getDraftsPosts(loggedIn);
        List<Dto_Posts> postDtos = draftPosts.stream().map(postMapper::toDto).toList();
        return ResponseEntity.ok(postDtos);
    }

    @PostMapping
    public ResponseEntity<Dto_Posts> post(
            @RequestBody Create_Post_Dto createpostdto,
            @AuthenticationPrincipal UserDetails userDetails) {
        Entity_User loggedInUser = getLoggedInUser(userDetails);
        CreatePostRequest createpost = postMapper.tocreatpostrequest(createpostdto);
        Entity_Post createdPost = postService.createPost(loggedInUser, createpost);
        Dto_Posts createdPostDto = postMapper.toDto(createdPost);
        return new ResponseEntity<>(createdPostDto, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Dto_Posts> updatePost(
            @PathVariable UUID id,
            @Valid @RequestBody Update_Post_Dto updatePostDto) {
        UpdatePostRequest updatePostRequest = postMapper.toupdatepostrequest(updatePostDto);
        Entity_Post updatedPost = postService.updatePost(id, updatePostRequest);
        Dto_Posts updatedPostDto = postMapper.toDto(updatedPost);
        return ResponseEntity.ok(updatedPostDto);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Dto_Posts> getPost(@PathVariable UUID id) {
        Entity_Post post = postService.getPost(id);
        Dto_Posts postDto = postMapper.toDto(post);
        return ResponseEntity.ok(postDto);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}