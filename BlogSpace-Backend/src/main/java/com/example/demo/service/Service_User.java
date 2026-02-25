package com.example.demo.service;

import com.example.demo.domain.entity.Entity_User;
import com.example.demo.repository.Repository_User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class Service_User {

    private final Repository_User repository_User;
    public Entity_User getUserById(UUID id){
        return repository_User.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

    }
}
