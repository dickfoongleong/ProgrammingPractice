package com.lafarleaf.planner.services;

import javax.transaction.Transactional;

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

    public boolean isUsernameAvailable(String username) {
        try {
            findByUsername(username);
            return false;
        } catch (UsernameNotFoundException unfe) {
            return true;
        }
    }

    public boolean isEmailAvailable(String email) {
        try {
            findByEmail(email);
            return false;
        } catch (EmailNotFoundException enfe) {
            return true;
        }
    }

    @Transactional
    public void add(User user) {
        repo.save(user);
    }

    @Transactional
    public void delete(User user) {
        repo.delete(user);
    }
}
