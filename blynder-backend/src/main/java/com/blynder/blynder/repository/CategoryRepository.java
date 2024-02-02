package com.blynder.blynder.repository;

import com.blynder.blynder.model.StreamCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<StreamCategory, Integer> {
    Page<StreamCategory> findAll(Pageable pageable);

    Optional<StreamCategory> findByCategoryName(String name);
}







