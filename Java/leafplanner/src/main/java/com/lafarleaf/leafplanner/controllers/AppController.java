package com.lafarleaf.leafplanner.controllers;

import java.util.ArrayList;
import java.util.List;

import com.lafarleaf.leafplanner.models.Alert;
import com.lafarleaf.leafplanner.models.Event;
import com.lafarleaf.leafplanner.models.User;
import com.lafarleaf.leafplanner.services.AlertService;
import com.lafarleaf.leafplanner.services.EventService;
import com.lafarleaf.leafplanner.services.UserService;
import com.lafarleaf.leafplanner.utils.UserEvent;

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
    private final AlertService alertService;
    private final EventService eventService;

    @Autowired
    public AppController(PasswordEncoder passwordEncoder, UserService userService, AlertService alertService,
            EventService eventService) {
        this.userService = userService;
        this.alertService = alertService;
        this.eventService = eventService;
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
    public List<UserEvent> get(@PathVariable("email") String email) {
        List<UserEvent> userEventList = new ArrayList<>();

        User user = userService.getByEmail(email);
        List<Event> events = eventService.getAllByUserId(user.getId());
        events.forEach((evt) -> {
            List<Alert> alerts = alertService.getAllByEventId(evt.getId());
            userEventList.add(new UserEvent(evt.getId(), evt.getUserId(), evt.getTitle(), evt.getDesc(),
                    evt.getLocation(), evt.getStartTime(), evt.getEndTime(), evt.getRepeat(), alerts));
        });

        return userEventList;
    }
}
