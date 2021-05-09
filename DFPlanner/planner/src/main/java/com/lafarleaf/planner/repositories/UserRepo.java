package com.lafarleaf.planner.repositories;

import java.util.Optional;

import com.lafarleaf.planner.models.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);
}
