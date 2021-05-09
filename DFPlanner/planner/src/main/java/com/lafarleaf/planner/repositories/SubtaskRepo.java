package com.lafarleaf.planner.repositories;

import java.util.List;

import com.lafarleaf.planner.models.Subtask;
import com.lafarleaf.planner.models.Task;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SubtaskRepo extends JpaRepository<Subtask, Integer> {
    List<Subtask> findByTask(Task task);
}
