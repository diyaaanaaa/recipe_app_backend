package com.recipe.model.dto;


import lombok.Data;
import java.util.List;

@Data
public class RecipeResponseDto {
    private int id;

    private String title;

    private String description;

    private String image;

    private List<RecipeIngridientResponseDto> ingridients;
}
