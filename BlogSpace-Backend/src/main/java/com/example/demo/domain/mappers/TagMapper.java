package com.example.demo.domain.mappers;

import com.example.demo.domain.dtos.Dto_Tags;
import com.example.demo.domain.entity.Entity_Post;
import com.example.demo.domain.entity.Entity_Tags;
import com.example.demo.domain.entity.Entity_Tags;
import com.example.demo.domain.Enum_Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper {

    @Mapping(target = "postCount", source = "posts", qualifiedByName = "calculatePostCount")
    Dto_Tags toTagResponse(Entity_Tags tag);

    @Named("calculatePostCount")
    default Integer calculatePostCount(List<Entity_Post> posts) {
        if (posts == null) {
            return 0;
        }
        return (int) posts.stream()
                .filter(post -> Enum_Post.PUBLISHED.equals(post.getStatus()))
                .count();
    }
}