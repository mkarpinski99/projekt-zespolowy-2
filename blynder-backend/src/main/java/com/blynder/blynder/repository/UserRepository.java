package com.blynder.blynder.repository;

import com.blynder.blynder.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    void deleteByUsername(String username);

    boolean existsByUsername(String username);
}
