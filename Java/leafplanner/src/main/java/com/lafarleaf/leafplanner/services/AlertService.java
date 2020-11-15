package com.lafarleaf.leafplanner.services;

import java.util.List;

import com.lafarleaf.leafplanner.models.Alert;
import com.lafarleaf.leafplanner.repositories.AlertRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlertService {
    private final AlertRepo repo;

    @Autowired
    public AlertService(AlertRepo repo) {
        this.repo = repo;
    }

    public List<Alert> getAllByEventId(int id) {
        return repo.getAllByEventId(id);
    }

}
