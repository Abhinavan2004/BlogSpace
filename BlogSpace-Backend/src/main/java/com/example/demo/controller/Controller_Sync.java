package com.example.demo.controller;

import com.example.demo.domain.entity.Entity_User;
import com.example.demo.repository.Repository_User;
import com.example.demo.security.BlogUserDetails;
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

    @PostMapping("/sync")
    public ResponseEntity<?> syncUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        Entity_User user = repository_user.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(Map.of(
                "id", user.getId().toString(),
                "email", user.getEmail(),
                "username", user.getUsername(),
                "auth0Id", user.getAuth0Id() != null ? user.getAuth0Id() : ""
        ));
    }
}