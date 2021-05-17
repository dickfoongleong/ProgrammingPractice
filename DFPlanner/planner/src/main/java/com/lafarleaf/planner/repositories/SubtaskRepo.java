package com.lafarleaf.planner.repositories;

import java.util.List;
import java.util.Optional;

import com.lafarleaf.planner.models.Subtask;
import com.lafarleaf.planner.models.Task;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SubtaskRepo extends JpaRepository<Subtask, Long> {
    List<Subtask> findByTask(Task task);

    Optional<Subtask> findByTaskAndId(Task task, long id);
}
