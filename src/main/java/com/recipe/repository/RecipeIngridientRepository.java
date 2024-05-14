package com.recipe.repository;

import com.recipe.model.dao.RecipeIngridient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeIngridientRepository extends JpaRepository<RecipeIngridient, Integer> {

    List<RecipeIngridient> findByRecipeId(int recipeId);
}
