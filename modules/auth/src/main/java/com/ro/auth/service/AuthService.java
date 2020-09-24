package com.ro.auth.service;

import com.ro.auth.controller.body.RegistrationRequest;
import com.ro.auth.controller.body.UserUpdateRequest;
import com.ro.auth.exception.UserAlreadyExistException;
import com.ro.auth.model.User;
import com.ro.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

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

  @Transactional
  public User updateInfo(User user, UserUpdateRequest updateRequest) {
      if (updateRequest.getName() != null) {
      user.setName(updateRequest.getName());
    }
    if (updateRequest.getPassword() != null) {
      user.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
    }
    return userRepository.save(user);
  }


  public User register(RegistrationRequest registrationRequest) {
    Optional<User> alreadyExistedUser = userRepository.findByEmailOrPhone(
        registrationRequest.getEmail(),
        registrationRequest.getPhone());
    if (alreadyExistedUser.isPresent()) {
      throw new UserAlreadyExistException();
    }

    User user = new User();
    user.setPhone(registrationRequest.getPhone());
    user.setEmail(registrationRequest.getEmail());
    user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
    user.setName(registrationRequest.getName());
    user.setBonuses(0);
    return userRepository.save(user);
  }
}
