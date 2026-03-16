package com.example.demo.controller;

import com.example.demo.domain.entity.Entity_User;
import com.example.demo.repository.Repository_User;
import com.example.demo.security.BlogUserDetails;
import com.example.demo.service.Service_Auth_Impl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class Controller_Sync {

    private final Repository_User repository_user;
    private final Service_Auth_Impl service_auth;

    @PostMapping("/sync")
    public ResponseEntity<?> syncUser(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String token = authHeader.substring(7);

        try {
            // Use your existing token validation service
            UserDetails userDetails = service_auth.validateToken(token);

            Entity_User user = repository_user.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            return ResponseEntity.ok(Map.of(
                    "id", user.getId().toString(),
                    "email", user.getEmail(),
                    "username", user.getUsername(),
                    "auth0Id", user.getAuth0Id() != null ? user.getAuth0Id() : ""
            ));
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid token");
        }
    }
}