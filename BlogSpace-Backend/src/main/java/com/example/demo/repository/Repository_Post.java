package com.example.demo.repository;

import com.example.demo.domain.Enum_Post;
import com.example.demo.domain.entity.Entity_Category;
import com.example.demo.domain.entity.Entity_Post;
import com.example.demo.domain.entity.Entity_Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface Repository_Post extends JpaRepository<Entity_Post, UUID> {
    List<Entity_Post> findAllByStatusAndCategoryAndTags(Enum_Post status, Entity_Category category, Entity_Tags tags);
}
