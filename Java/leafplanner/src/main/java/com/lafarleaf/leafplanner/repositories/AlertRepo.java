package com.lafarleaf.leafplanner.repositories;

import java.util.List;

import com.lafarleaf.leafplanner.models.Alert;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertRepo extends JpaRepository<Alert, Integer> {
    List<Alert> getAllByEventId(int id);
}
