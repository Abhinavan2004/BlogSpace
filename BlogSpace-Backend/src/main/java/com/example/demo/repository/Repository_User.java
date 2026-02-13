package com.example.demo.repository;

import com.example.demo.domain.entity.Entity_User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface Repository_User extends JpaRepository<Entity_User, UUID> {
    Entity_User findByUsername(String email);
}
