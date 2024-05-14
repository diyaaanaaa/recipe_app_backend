package com.recipe.repository;

import com.recipe.model.dao.Ingridient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IngridientRepository extends JpaRepository<Ingridient, Integer> {

    Optional<Ingridient> findByTitleIgnoreCase(String title);
}
