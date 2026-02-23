package com.example.demo.domain.mappers;

import com.example.demo.domain.dtos.Tags_Response;
import com.example.demo.domain.entity.Entity_Post;
import com.example.demo.domain.entity.Entity_Tags;
import com.example.demo.domain.Enum_Post;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper {

    @Mapping(target = "postCount", source = "posts", qualifiedByName = "calculatePostCount")
    Tags_Response toTagResponse(Entity_Tags tag);

    @Named("calculatePostCount")
    default Integer calculatePostCount(Set<Entity_Post> posts) {
        if (posts == null) {
            return 0;
        }
        return (int) posts.stream()
                .filter(post -> Enum_Post.PUBLISHED.equals(post.getStatus()))
                .count();
    }
}