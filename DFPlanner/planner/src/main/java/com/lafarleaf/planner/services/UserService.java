package com.lafarleaf.planner.services;

import com.lafarleaf.planner.models.User;
import com.lafarleaf.planner.repositories.UserRepo;
import com.lafarleaf.planner.utils.Exceptions.EmailNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepo repo;

    @Autowired
    public UserService(UserRepo repo) {
        this.repo = repo;
    }

    public User findByUsername(String username) throws UsernameNotFoundException {
        return repo.findByUsername(username) // Find by Username
                .orElseThrow(() -> // Throw error on not found
                new UsernameNotFoundException(String.format("Active username %s not found", username)));
    }

    public User findByEmail(String email) throws EmailNotFoundException {
        return repo.findByEmail(email) // Find by email
                .orElseThrow(() -> // If not found, throw Error.
                new EmailNotFoundException(String.format("Email %s not found", email)));

    }
}
