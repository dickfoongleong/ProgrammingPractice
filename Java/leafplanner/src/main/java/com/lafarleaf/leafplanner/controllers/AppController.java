package com.lafarleaf.leafplanner.controllers;

import java.util.List;
import java.util.Set;

import com.lafarleaf.leafplanner.models.Event;
import com.lafarleaf.leafplanner.models.User;
import com.lafarleaf.leafplanner.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/leafplanner/api/user")
public class AppController {
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Autowired
    public AppController(PasswordEncoder passwordEncoder, UserService userService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/all")
    public List<User> getAllUser() {
        return userService.getAll();
    }

    @PostMapping("/add")
    public User add(@RequestBody User user) {
        System.out.println(user.isEnabled());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userService.add(user);
    }

    @GetMapping("/{email}/events")
    @PreAuthorize("hasRole('ROLE_ADMIN' + #email)")
    public Set<Event> get(@PathVariable("email") String email) {
        User user = userService.getByEmail(email);
        return user.getEventSet();
    }
}
