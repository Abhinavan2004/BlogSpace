package com.example.demo.controller;

import com.example.demo.domain.dtos.AuthResponse;
import com.example.demo.service.Service_Auth;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class Controller_Auth {

    private final Service_Auth service_auth;

    @PostMapping
    public ResponseEntity<AuthResponse> login(@RequestBody UserDetails userDetails){
        UserDetails user = service_auth.authenticate(
                userDetails.getUsername(),
                userDetails.getPassword()
        );
        String tokenValue = service_auth.generateToken(user);

        AuthResponse resp = AuthResponse.builder()
                .token(tokenValue)
                .expiresIn(86400)
                .build();
        return ResponseEntity.ok(resp);
    }
}
