package com.example.demo.repository;

import com.example.demo.domain.entity.Entity_Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface Repository_Category extends JpaRepository<Entity_Category, UUID> {
}
