package com.recipe.repository;

import com.recipe.model.dao.UserRecipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRecipeRepository extends JpaRepository<UserRecipe, Integer> {

    Optional<UserRecipe> findByIdAndUser_Id(Integer recipeId, long userId);
}
