package com.example.demo.domain.dtos;

import com.example.demo.domain.Enum_Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Update_Post_Dto {

    @NotNull(message = " Post Id is required")
    private UUID id;

    @NotBlank(message = "Title is Required")
    @Size(min =3 , max =200 , message="Title must be between {min} and {max} characters")
    private String title;

    @NotBlank(message = "Content is Required")
    @Size(min =3 , max =20000 , message="Content must be between {min} and {max} characters")
    private String content;

    @NotNull(message = "Category ID is required")
    private UUID categoryId;

    @Builder.Default
    @Size(max =10 , message ="Maximum {max} tags allowed")
    private Set<UUID> tagIds ;

    @NotNull(message = "Status is required")
    private Enum_Post status ;






}
