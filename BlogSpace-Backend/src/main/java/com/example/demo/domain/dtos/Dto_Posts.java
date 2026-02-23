package com.example.demo.domain.dtos;

import com.example.demo.domain.Enum_Post;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dto_Posts {
    private UUID id;
    private String title;
    private String content;
    private Dto_Author author ;
    private Dto_Category category;
    private Set<Dto_Tag> tags;
    private Integer readingTime ;
    private LocalDateTime createdAt ;
    private LocalDateTime updatedAt ;
    private Enum_Post postStatus ;

}
