package com.recipe.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailDto {

    @NotBlank(message = "email is required")
    private String email;
}
