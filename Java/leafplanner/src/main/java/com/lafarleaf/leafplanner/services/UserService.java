package com.lafarleaf.leafplanner.services;

import java.util.List;

import com.lafarleaf.leafplanner.models.User;
import com.lafarleaf.leafplanner.repositories.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    private final UserRepo repo;

    @Autowired
    public UserService(UserRepo repo) {
        this.repo = repo;
    }

    public User getByEmail(String email) throws UsernameNotFoundException {
        return repo.findByEmail(email) // Find by email
                .orElseThrow(() -> // If not found, throw Error.
                new UsernameNotFoundException(String.format("Email %s not found", email)));
    }

    public User add(User user) {
        repo.save(user);
        return user;
    }

    public List<User> getAll() {
        return repo.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repo.findByEmail(email) // Find by email
                .orElseThrow(() -> // If not found, throw Error.
                new UsernameNotFoundException(String.format("Email %s not found", email)));
    }

}
