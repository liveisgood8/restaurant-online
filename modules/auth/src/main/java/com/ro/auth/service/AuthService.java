package com.ro.auth.service;

import com.ro.auth.controller.body.RegistrationRequest;
import com.ro.auth.exception.UserAlreadyExistException;
import com.ro.auth.model.User;
import com.ro.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  private AuthenticationManager authenticationManager;
  private UserRepository userRepository;
  private PasswordEncoder passwordEncoder;

  @Autowired
  public void setAuthenticationManager(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  @Autowired
  public void setUserRepository(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Autowired
  public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  public void authenticateUser(String username, String password) {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
  }

  public User register(RegistrationRequest registrationRequest) {
    if (userRepository.findByEmail(registrationRequest.getEmail()).isPresent()) {
      throw new UserAlreadyExistException(registrationRequest.getEmail());
    }

    User user = new User();
    user.setEmail(registrationRequest.getEmail());
    user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
    user.setName(registrationRequest.getName());
    user.setSurname(registrationRequest.getSurname());
    return userRepository.save(user);
  }
}
