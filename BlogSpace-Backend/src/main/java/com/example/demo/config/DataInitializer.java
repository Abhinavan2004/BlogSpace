package com.example.demo.config;

import com.example.demo.domain.entity.Entity_User;
import com.example.demo.repository.Repository_User;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final Repository_User repositoryUser;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        if (repositoryUser.findByEmail("user@test.com").isEmpty()) {
            Entity_User user = Entity_User.builder()
                    .email("user@test.com")
                    .username("Test User")
                    .password(passwordEncoder.encode("password"))
                    .build();

            repositoryUser.save(user);
        }
    }
}