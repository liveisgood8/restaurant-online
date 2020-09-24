package com.ro.auth.controller;

import com.ro.auth.controller.body.AuthRequest;
import com.ro.auth.controller.body.AuthResponse;
import com.ro.auth.controller.body.RegistrationRequest;
import com.ro.auth.controller.body.UserUpdateRequest;
import com.ro.auth.exception.UserAlreadyExistException;
import com.ro.auth.model.User;
import com.ro.auth.service.AuthService;
import com.ro.auth.service.JwtUserDetailsService;
import com.ro.auth.service.UserService;
import com.ro.auth.utils.JwtTokenUtil;
import com.ro.core.ApiError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
  private final AuthService authService;
  private final JwtUserDetailsService jwtUserDetailsService;
  private final JwtTokenUtil jwtTokenUtil;

  @Autowired
  public AuthController(AuthService authService,
                        JwtUserDetailsService jwtUserDetailsService,
                        JwtTokenUtil jwtTokenUtil) {
    this.authService = authService;
    this.jwtUserDetailsService = jwtUserDetailsService;
    this.jwtTokenUtil = jwtTokenUtil;
  }

  @PostMapping
  public AuthResponse authenticate(@RequestBody AuthRequest authRequest) {
    authService.authenticateUser(authRequest.getLogin(), authRequest.getPassword());

    User user = (User) jwtUserDetailsService.loadUserByUsername(authRequest.getLogin());
    String accessToken = jwtTokenUtil.generateToken(user);

    return new AuthResponse(accessToken, new AuthResponse.UserInfo(user));
  }

  @PatchMapping("/user-info")
  public User updateInfo(@RequestBody UserUpdateRequest updateRequest, Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    return authService.updateInfo(user, updateRequest);
  }

  @PostMapping("/registration")
  public User register(@Valid @RequestBody RegistrationRequest registrationRequest) {
    return authService.register(registrationRequest);
  }

  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @ExceptionHandler({UserAlreadyExistException.class})
  public ApiError handleUserAlreadyExist(UserAlreadyExistException ex) {
    return new ApiError(ex.getMessage());
  }

  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @ExceptionHandler({BadCredentialsException.class})
  public ApiError handleBadCredentials(BadCredentialsException ex) {
    return new ApiError("Неверный адрес эл. почты (номер телефон) или пароль");
  }
}
