package com.recipe.controller;


import com.recipe.model.dao.RecipeType;
import com.recipe.service.RecipeTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/recipe-type")
@RequiredArgsConstructor
public class RecipeTypeController {

    private final RecipeTypeService recipeTypeService;

    @GetMapping
    public ResponseEntity<List<RecipeType>> getAllTypes(){
        List<RecipeType> recipeTypeList = recipeTypeService.getAllTypes();

        return ResponseEntity.ok(recipeTypeList);
    }

}
