package com.blynder.blynder.repository;

import com.blynder.blynder.model.Stream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StreamRepository extends JpaRepository<Stream, Integer> {
    Page<Stream> findAll(Pageable pageable);
}
