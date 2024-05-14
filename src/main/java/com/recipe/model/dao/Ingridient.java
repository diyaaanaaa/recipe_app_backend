package com.recipe.model.dao;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name="ingridients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ingridient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    @Column(length = 100)
    private String title;

    @JoinColumn(name = "ingridient_type")
    @ManyToOne
    private IngridientType ingridientType;

}
