package com.ro.auth.service;

import com.ro.auth.controller.body.RegistrationRequest;
import com.ro.auth.controller.body.UserUpdateRequest;
import com.ro.auth.exception.UserAlreadyExistException;
import com.ro.auth.model.User;
import com.ro.auth.oauth2.AuthProvider;
import com.ro.auth.oauth2.user.info.OAuth2UserInfo;
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
  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public AuthService(AuthenticationManager authenticationManager,
                     UserRepository userRepository,
                     PasswordEncoder passwordEncoder) {
    this.authenticationManager = authenticationManager;
    this.userRepository = userRepository;
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
      user.setIsCredentialsExpired(false);
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
    user.setAuthProvider(AuthProvider.NATIVE);
    return userRepository.save(user);
  }
}
