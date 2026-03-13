package com.example.demo.service;

import com.example.demo.domain.entity.Entity_User;
import com.example.demo.repository.Repository_User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.nio.charset.StandardCharsets;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class Service_Auth_Impl implements Service_Auth{

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final Repository_User userRepository;

    @Value("${jwt.secret}")
    private String SecretKey ;

    @Override
    public UserDetails authenticate(String email, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        System.out.println("RAW PASSWORD: " + password);
        System.out.println("DB PASSWORD: " + userDetails.getPassword());

        boolean matches = passwordEncoder.matches(password, userDetails.getPassword());
        System.out.println("PASSWORD MATCHES: " + matches);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        return userDetails;
    }


    public void register(String email, String password) {

        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        Entity_User user = Entity_User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .username(email)
                .build();

        userRepository.save(user);
    }



    private long jwtExpiryms = 86400000L;

    @Override
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiryms))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(
                SecretKey.getBytes(StandardCharsets.UTF_8)
        );
    }

    public UserDetails validateToken(String token){
        String username = extractUsername(token);
        return userDetailsService.loadUserByUsername(username);
    }

    private String extractUsername(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}
