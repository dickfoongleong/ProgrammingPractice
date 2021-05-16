package com.lafarleaf.planner.controllers;

import java.util.HashMap;
import java.util.Map;


import com.lafarleaf.planner.models.User;
import com.lafarleaf.planner.services.MailService;
import com.lafarleaf.planner.services.UserService;
import com.lafarleaf.planner.utils.Exceptions.EmailNotSentException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dfplanner/setup/")
public class UserSetupController {
    private static final String ACTIVATE_USER_URL = "http://localhost:8080/dfplanner/setup/activate?username=%s";

    private final PasswordEncoder passwordEncoder;

    private final UserService userService;
    private final MailService mailService;

    @Autowired
    public UserSetupController(PasswordEncoder passwordEncoder, UserService userService, MailService mailService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.mailService = mailService;
    }

    @PostMapping("register")
    public Map<String, Object> registerUser(@RequestBody User user) {
        Map<String, Object> responses = new HashMap<>();
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        boolean isUsernameAvailable = userService.isUsernameAvailable(user.getUsername());
        boolean isEmailAvailable = userService.isEmailAvailable(user.getEmail());

        if (isUsernameAvailable && isEmailAvailable) {
            try {
                userService.add(user);
                user = userService.findByUsername(user.getUsername());
                sendActivationMail(user);

                responses.put("result", true);
            } catch (AuthenticationException ae) {
                responses.put("result", false);
                responses.put("message", "Failed to register " + user.getUsername());
            } catch (EmailNotSentException ense) {
                responses.put("result", false);
                responses.put("message", ense.getMessage());
                userService.delete(user);
            }
        }
        return responses;
    }

    @PostMapping("login")
    public Map<String, Object> loginUser(@RequestBody Map<String, String> params) {
        Map<String, Object> responses = new HashMap<>();
        responses.put("result", false);

        if (params.size() != 2 || !isCredProvided(params)) {
            responses.put("message", "Invalid parameters passed.");
            return responses;
        }

        try {
            String username = params.get("username");
            String password = params.get("password");

            User user = userService.findByUsername(username);
            if (user.isEnabled() && passwordEncoder.matches(password, user.getPassword())) {
                responses.put("result", true);
            } else if (!user.isEnabled()) {
                responses.put("message", "Inactive user.");
                sendActivationMail(user);
            } else {
                responses.put("message", "Incorrect password.");
            }
        } catch (AuthenticationException ae) {
            responses.put("message", ae.getMessage());
        } catch (EmailNotSentException ense) {
            responses.put("message", ense.getMessage());
        }

        return responses;
    }

    @PostMapping("activate")
    public Map<String, Object> activateUser(@RequestBody Map<String, String> params) {
        Map<String, Object> responses = new HashMap<>();
        responses.put("result", false);

        if (params.size() != 2 || !isCredProvided(params)) {
            responses.put("message", "Invalid parameters passed.");
            return responses;
        }

        try {
            String username = params.get("username");
            String password = params.get("password");

            User user = userService.findByUsername(username);
            if (passwordEncoder.matches(password, user.getPassword())) {
                responses.put("result", true);
                user.setEnabled(true);
                userService.add(user);
                sendActivatedMail(user);
            } else {
                responses.put("message", "Incorrect password.");
            }
        } catch (AuthenticationException ae) {
            responses.put("message", ae.getMessage());
        } catch (EmailNotSentException ense) {
            responses.put("message", ense.getMessage());
        }

        return responses;
    }

    private boolean isCredProvided(Map<String, String> params) {
        return params.containsKey("username") && params.containsKey("password");
    }

    private void sendActivationMail(User user) throws EmailNotSentException {
        String registrationLink = String.format(ACTIVATE_USER_URL, user.getUsername());
        String emailMsg = String.format(
                "Thank you for choosing DF Planner as your planning solution!\n\nClick the following link to confirm your registration.\n%s",
                registrationLink);
        mailService.sendMail(user.getEmail(), "Activate Your Account", emailMsg, null);
    }

    private void sendActivatedMail(User user) throws EmailNotSentException {
        String emailMsg = "Your account is activated, please login and start traking your tasks with DF Planner!";
        mailService.sendMail(user.getEmail(), "Account Is Activated", emailMsg, null);
    }
}
