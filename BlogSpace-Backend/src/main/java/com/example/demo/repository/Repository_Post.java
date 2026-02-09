package com.example.demo.repository;

import com.example.demo.domain.entity.Entity_Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface Repository_Post extends JpaRepository<Entity_Post, UUID> {
}
