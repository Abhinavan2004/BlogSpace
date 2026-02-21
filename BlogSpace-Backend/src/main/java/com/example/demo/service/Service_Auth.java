package com.example.demo.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface Service_Auth {
    UserDetails authenticate(String email, String password);
    String generateToken(UserDetails userDetails);
    UserDetails validateToken(String token);
}
