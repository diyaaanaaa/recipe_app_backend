package com.recipe.model.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class RecipeIngridientId implements Serializable {

    @Column(name = "recipe_id")
    int recipeId;

    @Column(name="ingridient_id")
    int ingridientId;
}
