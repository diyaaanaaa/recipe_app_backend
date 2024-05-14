package com.recipe.repository;

import com.recipe.model.dao.RecipeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeTypeRespository extends JpaRepository<RecipeType, Integer> {
}
