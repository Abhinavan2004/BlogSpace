package com.example.demo.domain.dtos;

import com.example.demo.domain.Enum_Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Create_Post {

    @NotBlank(message = "Title is required")
    @Size(min = 3 , max = 200 , message="Contente must be between {min} and {max} characters")
    private String title;

    @NotBlank(message = "Contnet is required")
    @Size(min = 3 , max = 1000 , message="Contente must be between {min} and {max} characters")
    private String content;

    @NotNull(message = "Category ID is required")
    private UUID categoryId;

    @Builder.Default@Size(max=10 , message ="Maximum {max} tags allowed")
    private Set<UUID> tagIds = new HashSet<>();

    @NotNull(message = "Status is required")
    private Enum_Post staus ;


}
