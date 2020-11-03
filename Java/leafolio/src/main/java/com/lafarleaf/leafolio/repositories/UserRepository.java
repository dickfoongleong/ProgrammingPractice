package com.lafarleaf.leafolio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lafarleaf.leafolio.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
