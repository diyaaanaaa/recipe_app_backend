package com.recipe.service;

import com.recipe.model.dto.RecipeResponseDto;

import java.util.List;

public interface RecipeService {
    List<RecipeResponseDto> getRecipesByTitle(String title);
    List<RecipeResponseDto> getRecipesByIngridients(List<String> ingridientNames);
}
