package com.ro.auth.controller;

import com.ro.auth.controller.body.AuthRequest;
import com.ro.auth.controller.body.AuthResponse;
import com.ro.auth.model.User;
import com.ro.auth.service.AuthService;
import com.ro.auth.service.JwtUserDetailsService;
import com.ro.auth.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
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
  public void setAuthService(AuthService authService) {
    this.authService = authService;
  }

  @Autowired
  public void setJwtUserDetailsService(JwtUserDetailsService jwtUserDetailsService) {
    this.jwtUserDetailsService = jwtUserDetailsService;
  }

  @Autowired
  public void setJwtTokenUtil(JwtTokenUtil jwtTokenUtil) {
    this.jwtTokenUtil = jwtTokenUtil;
  }

  @PostMapping
  public AuthResponse authenticate(@RequestBody AuthRequest authRequest) {
    authService.authenticateUser(authRequest.getLogin(), authRequest.getPassword());

    UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(authRequest.getLogin());
    String accessToken = jwtTokenUtil.generateToken(userDetails);

    AuthResponse authResponse = new AuthResponse();
    authResponse.setAccessToken(accessToken);

    return authResponse;
  }

  @PostMapping("/registration")
  public void register(@RequestBody User user) {
    authService.register(user);
  }
}
