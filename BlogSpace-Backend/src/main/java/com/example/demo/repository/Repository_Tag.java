package com.example.demo.repository;

import com.example.demo.domain.entity.Entity_Tags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface Repository_Tag extends JpaRepository<Entity_Tags, UUID> {
    @Query("Select t from Entity_Tags t Left join fetch t.posts")
    List<Entity_Tags> findAllWithPostCount();

    @Query("")
    List<Entity_Tags> findByNameIn(Set<String> tagNames);
}
