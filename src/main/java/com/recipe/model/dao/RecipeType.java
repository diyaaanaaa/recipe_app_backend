package com.recipe.model.dao;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name="r_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    @Column(length = 100)
    private String type;

    @NotBlank
    @Column(length = 255)
    private String image;
}
