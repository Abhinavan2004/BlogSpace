package com.example.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class Service_Auth_Impl implements Service_Auth{
    @Override
    public UserDetails authenticate(String email, String password) {
        return null;
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        return "";
    }
}
