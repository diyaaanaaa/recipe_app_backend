package com.recipe.controller;

import com.recipe.model.dto.RecipeResponseDto;
import com.recipe.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping("/by-title")
    public ResponseEntity<List<RecipeResponseDto>> getRecipesByTitle(@RequestParam("title") String title){

        List<RecipeResponseDto> recipes = recipeService.getRecipesByTitle(title);

        return ResponseEntity.ok(recipes);
    }

    @GetMapping("/by-ingredients")
    public ResponseEntity<List<RecipeResponseDto>> getRecipesByTitle(@RequestParam List<String> ingridients){

        List<RecipeResponseDto> recipes = recipeService.getRecipesByIngridients(ingridients);

        return ResponseEntity.ok(recipes);
    }
}
