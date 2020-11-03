package com.lafarleaf.leafolio.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lafarleaf.leafolio.models.User;
import com.lafarleaf.leafolio.repositories.UserRepository;

@Service
public class UserService {
  
  @Autowired
  private UserRepository userRepo;
  
  public List<User> showAll() {
    return userRepo.findAll();
  }
  
  public Optional<User> getUser(long id) {
    return userRepo.findById(id);
  }

  public void addUser(User user) {
    userRepo.save(user);
  }
}
