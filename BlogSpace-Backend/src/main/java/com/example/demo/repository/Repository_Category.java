package com.example.demo.repository;

import com.example.demo.domain.entity.Entity_Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface Repository_Category extends JpaRepository<Entity_Category, UUID> {
}
