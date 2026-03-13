package com.example.demo.security;

import com.example.demo.domain.entity.Entity_User;
import com.example.demo.repository.Repository_User;
import com.example.demo.service.Service_Auth_Impl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final Repository_User repository_user;
    private final Service_Auth_Impl service_auth;
    private final UserDetailsService userDetailsService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

        String email = oauth2User.getAttribute("email");
        String name  = oauth2User.getAttribute("name");
        String googleId = oauth2User.getAttribute("sub"); // Google's unique user ID

        // Find or create user in your DB
        Entity_User user = repository_user.findByEmail(email)
                .orElseGet(() -> {
                    Entity_User newUser = Entity_User.builder()
                            .email(email)
                            .username(name)
                            .auth0Id(googleId)          // reusing auth0Id field for Google sub
                            .password(UUID.randomUUID().toString()) // dummy password, never used
                            .build();
                    return repository_user.save(newUser);
                });

        // Generate your JWT the same way as email/password login
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        String token = service_auth.generateToken(userDetails);

        // Redirect to frontend with token in URL
        response.sendRedirect("http://localhost:5173/oauth2/callback?token=" + token);
    }
}