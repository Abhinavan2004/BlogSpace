package com.example.demo.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Dto_CreateCategory {
    @NotBlank(message="Category name is required")
    @Size(min=2 , max=10 , message = "Name should be between {min} to {max} letters")
    @Pattern(regexp="^[\\w\\s-]+$" , message="Name should only contain letters , digits , spaces and hypens")
    private String name;
}
