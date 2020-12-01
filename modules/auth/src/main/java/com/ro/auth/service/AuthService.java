package com.ro.auth.service;

import com.ro.auth.data.lib.AuthenticateData;
import com.ro.auth.exception.UserAlreadyExistException;
import com.ro.auth.data.model.AuthProvider;
import com.ro.auth.data.model.User;
import com.ro.auth.oauth2.exception.OAuth2ProviderNotExistException;
import com.ro.auth.data.repository.AuthProviderRepository;
import com.ro.auth.data.repository.UserRepository;
import com.ro.auth.utils.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class AuthService {
  private final static Logger logger = LoggerFactory.getLogger(AuthService.class);
  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;
  private final AuthProviderRepository authProviderRepository;
  private final JwtUserDetailsService jwtUserDetailsService;
  private final JwtTokenUtil jwtTokenUtil;

  @Autowired
  public AuthService(AuthenticationManager authenticationManager,
                     UserRepository userRepository,
                     AuthProviderRepository authProviderRepository,
                     JwtUserDetailsService jwtUserDetailsService,
                     JwtTokenUtil jwtTokenUtil) {
    this.authenticationManager = authenticationManager;
    this.userRepository = userRepository;
    this.authProviderRepository = authProviderRepository;
    this.jwtUserDetailsService = jwtUserDetailsService;
    this.jwtTokenUtil = jwtTokenUtil;
  }

  public AuthenticateData authenticateUser(String username, String password) {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

    User user = (User) jwtUserDetailsService.loadUserByUsername(username);
    String accessToken = jwtTokenUtil.generateToken(user);

    return new AuthenticateData(user, accessToken);
  }

  @Transactional
  public User update(User user) {
    return userRepository.save(user);
  }

  public User register(User user) {
    if (isUserExist(user)) {
      throw new UserAlreadyExistException();
    }

    AuthProvider authProvider = authProviderRepository.findByName(AuthProvider.NATIVE)
        .orElseThrow(() -> new OAuth2ProviderNotExistException(AuthProvider.NATIVE));

    user.setAuthProvider(authProvider);
    return userRepository.save(user);
  }

  private boolean isUserExist(User user) {
    return userRepository.existsByEmailOrTelephoneNumberCountryCodeAndTelephoneNumberNationalNumber(user.getEmail(),
        user.getTelephoneNumber().getCountryCode(),
        user.getTelephoneNumber().getNationalNumber());
  }
}
