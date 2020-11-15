package com.lafarleaf.leafplanner.repositories;

import java.util.List;

import com.lafarleaf.leafplanner.models.Event;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepo extends JpaRepository<Event, Integer> {
    List<Event> findAllByUserId(int id);
}
