package com.ro.auth.controller;

import com.ro.auth.controller.body.AuthRequest;
import com.ro.auth.controller.body.AuthResponse;
import com.ro.auth.controller.body.RegistrationRequest;
import com.ro.auth.model.User;
import com.ro.auth.service.AuthService;
import com.ro.auth.service.JwtUserDetailsService;
import com.ro.auth.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
  private AuthService authService;
  private JwtUserDetailsService jwtUserDetailsService;
  private JwtTokenUtil jwtTokenUtil;

  @Autowired
  public AuthController(AuthService authService, JwtUserDetailsService jwtUserDetailsService, JwtTokenUtil jwtTokenUtil) {
    this.authService = authService;
    this.jwtUserDetailsService = jwtUserDetailsService;
    this.jwtTokenUtil = jwtTokenUtil;
  }

  @PostMapping
  public AuthResponse authenticate(@RequestBody AuthRequest authRequest) {
    authService.authenticateUser(authRequest.getEmail(), authRequest.getPassword());

    User user = (User) jwtUserDetailsService.loadUserByUsername(authRequest.getEmail());
    String accessToken = jwtTokenUtil.generateToken(user);

    return new AuthResponse(accessToken, new AuthResponse.UserInfo(user));
  }

  @PostMapping("/registration")
  public User register(@RequestBody RegistrationRequest registrationRequest) {
    return authService.register(registrationRequest);
  }
}
