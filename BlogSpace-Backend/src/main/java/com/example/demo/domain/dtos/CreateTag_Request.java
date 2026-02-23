package com.example.demo.domain.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateTag_Request {
    @NotEmpty(message = "At least one tag should be used")
    @Size(max = 10, message = "Maximum {max} tags are allowed")
    private Set<
                @Size(min = 2, max = 100, message = "Tag should be between {min} and {max} characters")
                @Pattern(regexp = "^$[\\w\\s-]+$", message = "Tag name should only use letters , numbers and hypens")
                        String> names ;
}
