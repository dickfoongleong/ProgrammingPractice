package com.lafarleaf.planner.services;

import java.util.List;

import javax.transaction.Transactional;

import com.lafarleaf.planner.models.Subtask;
import com.lafarleaf.planner.models.Task;
import com.lafarleaf.planner.repositories.SubtaskRepo;
import com.lafarleaf.planner.utils.Exceptions.TaskNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubtaskService {
    private final SubtaskRepo repo;

    @Autowired
    public SubtaskService(SubtaskRepo repo) {
        this.repo = repo;
    }

    public List<Subtask> findByTask(Task task) {
        return repo.findByTask(task);
    }

    public Subtask findByTaskAndId(Task task, long id) throws TaskNotFoundException {
        return repo.findByTaskAndId(task, id) // Find by Subtask ID
                .orElseThrow(() -> // Throws error if not found.
                new TaskNotFoundException("Subtask not found."));
    }

    @Transactional
    public void add(Subtask subtask) {
        repo.save(subtask);
    }

    @Transactional
    public void delete(Subtask subtask) {
        repo.delete(subtask);
    }
}
