package com.lafarleaf.planner.services;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import com.lafarleaf.planner.models.Task;
import com.lafarleaf.planner.models.User;
import com.lafarleaf.planner.repositories.TaskRepo;
import com.lafarleaf.planner.utils.Exceptions.TaskNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
    private final TaskRepo repo;

    @Autowired
    public TaskService(TaskRepo repo) {
        this.repo = repo;
    }

    public List<Task> findByUser(User user) {
        return repo.findByUser(user);
    }

    public List<Task> findByUserAndDueDate(User user, Date dueDate) {
        return repo.findByUserAndDueDate(user, dueDate);
    }

    public Task findById(int id) throws TaskNotFoundException {
        return repo.findById(id) // Find by Task ID.
                .orElseThrow(() -> // Throws error if not found
                new TaskNotFoundException(String.format("Task ID %d not found.", id)));
    }

    @Transactional
    public void add(Task task) {
        repo.save(task);
    }

    @Transactional
    public void delete(Task task) {
        repo.delete(task);
    }
}
