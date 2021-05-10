package com.lafarleaf.planner.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

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

    @Transactional
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
    public Map<String, Object> loginUser(@RequestBody Map<String, String> parms) {
        Map<String, Object> responses = new HashMap<>();
        responses.put("result", false);

        if (parms.size() != 2 || !parms.containsKey("username") || !parms.containsKey("password")) {
            responses.put("message", "Invalid parameters passed.");
            return responses;
        }

        try {
            String username = parms.get("username");
            String password = parms.get("password");

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
        }

        return responses;
    }

    private void sendActivationMail(User user) throws EmailNotSentException {
        String registrationLink = String.format(ACTIVATE_USER_URL, user.getUsername());
        String emailMsg = String.format(
                "Thank you for choosing DF Planner as your planning solution!\n\nClick the following link to confirm your registration.\n%s",
                registrationLink);
        mailService.sendMail(user.getEmail(), "Activate Your Account", emailMsg, null);
    }
}
