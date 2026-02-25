package com.example.demo.domain.mappers;

import com.example.demo.domain.CreatePostRequest;
import com.example.demo.domain.dtos.Create_Post_Dto;
import com.example.demo.domain.dtos.Dto_Posts;
import com.example.demo.domain.entity.Entity_Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring" , unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {
    @Mapping(target = "author" , source = "author")
    @Mapping(target="category" , source = "category")
    @Mapping(target="tags" , source="tags")
    Dto_Posts toDto(Entity_Post post);

    CreatePostRequest tocreatpostrequest(Create_Post_Dto dto);
}
