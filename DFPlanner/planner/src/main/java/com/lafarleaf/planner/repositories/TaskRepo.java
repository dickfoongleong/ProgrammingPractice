package com.lafarleaf.planner.repositories;

import java.util.Date;
import java.util.List;

import com.lafarleaf.planner.models.Task;
import com.lafarleaf.planner.models.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepo extends JpaRepository<Task, Integer> {
    List<Task> findByUser(User user);

    List<Task> findByUserAndDueDate(User user, Date dueDate);
}