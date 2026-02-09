package com.example.demo.repository;

import com.example.demo.domain.entity.Entity_Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface Repository_Category extends JpaRepository<Entity_Category, UUID> {

    @Query("""
        SELECT DISTINCT c
        FROM Entity_Category c
        LEFT JOIN FETCH c.posts
    """)
    List<Entity_Category> findAllWithPosts();
}

