package com.lafarleaf.leafplanner.repositories;

import java.util.Optional;

import com.lafarleaf.leafplanner.models.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}
