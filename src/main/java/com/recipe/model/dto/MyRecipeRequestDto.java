package com.recipe.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MyRecipeRequestDto {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

}
