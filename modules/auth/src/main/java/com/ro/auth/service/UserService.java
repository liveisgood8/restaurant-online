package com.ro.auth.service;

import com.ro.auth.model.User;
import com.ro.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserService {
  private final UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Transactional
  public User addBonuses(Long userId, Integer additionalBonuses) {
    Optional<User> user = userRepository.findById(userId);
    if (user.isEmpty()) {
      throw new EntityNotFoundException("User with id: " + userId + " not founded");
    }

    user.get().setBonuses(user.get().getBonuses() + additionalBonuses);
    return userRepository.save(user.get());
  }
}
