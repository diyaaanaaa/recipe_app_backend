package com.recipe.service.impl;

import com.recipe.model.dao.RecipeType;
import com.recipe.repository.RecipeTypeRespository;
import com.recipe.service.RecipeTypeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RecipeTypeServiceImpl implements RecipeTypeService {

    private final RecipeTypeRespository recipeTypeRespository;

    @Override
    public List<RecipeType> getAllTypes() {
        return recipeTypeRespository.findAll();
    }
}
