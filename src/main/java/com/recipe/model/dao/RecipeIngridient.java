package com.recipe.model.dao;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name="recipe_ingridients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeIngridient {

    @EmbeddedId
    RecipeIngridientId recipeIngridientId;

    @ManyToOne
    @MapsId("recipeId")
    @JoinColumn(name = "recipe_id")
    Recipe recipe;

    @ManyToOne
    @MapsId("ingridientId")
    @JoinColumn(name = "ingridient_id")
    Ingridient ingridient;

    @NotBlank
    private String measurement;
}
