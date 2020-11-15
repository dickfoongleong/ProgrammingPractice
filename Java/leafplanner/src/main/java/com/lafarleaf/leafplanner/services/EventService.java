package com.lafarleaf.leafplanner.services;

import java.util.List;

import com.lafarleaf.leafplanner.models.Event;
import com.lafarleaf.leafplanner.repositories.EventRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService {
    private final EventRepo repo;

    @Autowired
    public EventService(EventRepo repo) {
        this.repo = repo;
    }

    public List<Event> getAllByUserId(int id) {
        return repo.findAllByUserId(id);
    }
}
