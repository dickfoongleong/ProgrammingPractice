package com.lafarleaf.leafolio.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.lafarleaf.leafolio.models.User;
import com.lafarleaf.leafolio.services.UserService;

@RestController
@RequestMapping(value = {"/users"})
@CrossOrigin(origins = "*")
public class UserController {
  @Autowired
  private UserService userService;
  
  @GetMapping
  public List<User> showAll() {
    return userService.showAll();
  }
  
  @GetMapping("/{id}")
  public Optional<User> getPage(@PathVariable long id) {
    return userService.getUser(id);
  }
  
  @PostMapping()
  public void addUser(@RequestBody Map<String, String> param) {
    userService.addUser(new User(param.get("fname"), param.get("lname"), param.get("email"), param.get("password")));
  }
}
