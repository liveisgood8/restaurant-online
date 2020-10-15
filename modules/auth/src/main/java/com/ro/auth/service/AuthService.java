package com.ro.auth.service;

import com.ro.auth.dto.mappers.UserDtoMapper;
import com.ro.auth.dto.objects.UserDto;
import com.ro.auth.exception.UserAlreadyExistException;
import com.ro.auth.model.User;
import com.ro.auth.oauth2.AuthProvider;
import com.ro.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class AuthService {
  private final static Logger logger = LoggerFactory.getLogger(AuthService.class);
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
  public User updateInfo(User user, UserDto userDto) {
    UserDtoMapper.INSTANCE.mergeWithDto(user, userDto);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepository.save(user);
  }

  public User register(UserDto userDto) {
    User user = UserDtoMapper.INSTANCE.toEntity(userDto);
    if (isUserExist(user)) {
      throw new UserAlreadyExistException();
    }
    user.setPassword(passwordEncoder.encode(userDto.getPassword()));
    user.setBonuses(0);
    user.setAuthProvider(AuthProvider.NATIVE);
    return userRepository.save(user);
  }

  private boolean isUserExist(User user) {
    return userRepository.existsByEmailOrTelephoneNumberCountryCodeAndTelephoneNumberNationalNumber(user.getEmail(),
        user.getTelephoneNumber().getCountryCode(),
        user.getTelephoneNumber().getNationalNumber());
  }
}
