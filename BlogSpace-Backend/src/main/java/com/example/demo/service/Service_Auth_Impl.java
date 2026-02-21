package com.example.demo.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class Service_Auth_Impl implements Service_Auth{

    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;

    @Value("${jwt.secret}")
    private String SecretKey ;

    @Override
    public UserDetails authenticate(String email, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        return  userDetailsService.loadUserByUsername(email);
    }

    private long jwtExpiryms = 86400000L;

    @Override
    public String generateToken(UserDetails userDetails) {
        Map<String , Object> claims = new HashMap<>();
        return  Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiryms))
                .signWith(getSigningKey() , SignatureAlgorithm.HS256)
                .compact();
    };

    private Key getSigningKey() {
        byte []bytekeys = SecretKey.getBytes();
    return Keys.hmacShaKeyFor(bytekeys);
    }
}
