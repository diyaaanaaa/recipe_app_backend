package com.recipe.repository;

import com.recipe.model.dao.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {

    List<Recipe> findByTitleLikeIgnoreCase(String title);

    @Query(nativeQuery = true, value = "SELECT recipe_id AS id, title, `description`, image " +
            "FROM recipe_ingridients ri " +
            "LEFT JOIN recipe r ON r.id = ri.recipe_id  GROUP BY recipe_id " +
            "HAVING (SELECT COUNT(ingridient_id) FROM recipe_ingridients ri1 WHERE ingridient_id IN (?) AND ri1.recipe_id = ri.recipe_id) = ?")
    List<Recipe> findByRecipeIngridients(List<Integer> ingridients, int listSize);


}
