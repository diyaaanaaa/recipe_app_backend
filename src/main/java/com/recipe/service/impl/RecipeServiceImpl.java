package com.recipe.service.impl;


import com.recipe.model.dao.Recipe;
import com.recipe.model.dao.RecipeIngridient;
import com.recipe.model.dto.RecipeIngridientResponseDto;
import com.recipe.model.dto.RecipeResponseDto;
import com.recipe.repository.IngridientRepository;
import com.recipe.repository.RecipeIngridientRepository;
import com.recipe.repository.RecipeRepository;
import com.recipe.service.RecipeService;
import com.recipe.util.ErrorMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    private final IngridientRepository ingridientRepository;

    private final RecipeIngridientRepository recipeIngridientRepository;

    @Override
    public List<RecipeResponseDto> getRecipesByTitle(String title) {
        List<Recipe> recipes = recipeRepository.findByTitleLikeIgnoreCase("%"+title+"%");

        List<RecipeResponseDto> recipeResponseDtos = recipes.stream()
                .map(recipe -> {
                    RecipeResponseDto recipeResponseDto = new RecipeResponseDto();

                    recipeResponseDto.setId(recipe.getId());
                    recipeResponseDto.setTitle(recipe.getTitle());
                    recipeResponseDto.setDescription(recipe.getDescription());
                    recipeResponseDto.setImage(recipe.getImage());

                    List<RecipeIngridient> recipeIngridients = recipeIngridientRepository.findByRecipeId(recipe.getId());

                    List<RecipeIngridientResponseDto> recipeResponseDtoList = recipeIngridients.stream()
                                    .map(recipeIngridient -> {
                                        RecipeIngridientResponseDto recipeIngridientResponseDto = new RecipeIngridientResponseDto();

                                        recipeIngridientResponseDto.setIngridientName(recipeIngridient.getIngridient().getTitle());
                                        recipeIngridientResponseDto.setMeasurement(recipeIngridient.getMeasurement());

                                        return recipeIngridientResponseDto;
                                    }).toList();

                    recipeResponseDto.setIngridients(recipeResponseDtoList);

                    return recipeResponseDto;
                }).toList();

        return recipeResponseDtos;
    }

    @Override
    public List<RecipeResponseDto> getRecipesByIngridients(List<String> ingridientNames) {
        List<Integer> ingridients = ingridientNames.stream()
                .map((name)->ingridientRepository.findByTitleIgnoreCase(name).orElseThrow
                        (()->new RuntimeException(name+" "+ErrorMessages.INGREDIENT_NOT_FOUND)))
                .map((i)->i.getId())
                .toList();

        List<Recipe> recipeList = recipeRepository.findByRecipeIngridients(ingridients, ingridients.size());

        List<RecipeResponseDto> recipeResponseDtos = recipeList
                .stream()
                .map(recipe -> {
                    RecipeResponseDto recipeResponseDto = new RecipeResponseDto();

                    recipeResponseDto.setId(recipe.getId());
                    recipeResponseDto.setTitle(recipe.getTitle());
                    recipeResponseDto.setDescription(recipe.getDescription());
                    recipeResponseDto.setImage(recipe.getImage());

                    List<RecipeIngridient> recipeIngridients = recipeIngridientRepository.findByRecipeId(recipe.getId());

                    List<RecipeIngridientResponseDto> recipeResponseDtoList = recipeIngridients.stream()
                            .map(recipeIngridient -> {
                                RecipeIngridientResponseDto recipeIngridientResponseDto = new RecipeIngridientResponseDto();

                                recipeIngridientResponseDto.setIngridientName(recipeIngridient.getIngridient().getTitle());
                                recipeIngridientResponseDto.setMeasurement(recipeIngridient.getMeasurement());

                                return recipeIngridientResponseDto;
                            }).toList();

                    recipeResponseDto.setIngridients(recipeResponseDtoList);

                    return recipeResponseDto;
                }).toList();


        return recipeResponseDtos;
    }


}
