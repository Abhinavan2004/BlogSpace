package com.example.demo.domain.mappers;

import com.example.demo.domain.Enum_Post;
import com.example.demo.domain.dtos.Dto_Category;
import com.example.demo.domain.dtos.Dto_CreateCategory;
import com.example.demo.domain.entity.Entity_Category;
import com.example.demo.domain.entity.Entity_Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface Category_Mapper {

    @Mapping(target = "postCount", source="posts", qualifiedByName = "calculatePostCount")
    Dto_Category toDto(Entity_Category category);

    Entity_Category toEntity(Dto_CreateCategory dto_createCategory);

    @Named("calculatePostCount")
    default long calculatePostCount(List<Entity_Post> posts) {
        if (null == posts) {
            return 0;
        }
            return posts.stream()
                    .filter(post -> Enum_Post.PUBLISHED.equals(post.getStatus()))
                    .count();
        }
    }
