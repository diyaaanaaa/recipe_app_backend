package com.recipe.model.dao;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Entity(name="recipe")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    @Column(length = 255)
    private String title;

    @NotBlank
    @Column
    private String description;

    @NotBlank
    @Column
    private String image;

   // @JsonIgnore
    @OneToMany(mappedBy = "recipe")
    Set<RecipeIngridient> recipeIngridients;

}
