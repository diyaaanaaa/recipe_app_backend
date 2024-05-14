package com.recipe.model.dao;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Data;


@Entity
@Data
public class Role {

    @Id
    private int id;

    @NotNull
    @Size(max = 50)
    private String name;


}
