package com.ro.auth.controller;

import com.ro.auth.controller.body.AuthRequest;
import com.ro.auth.controller.body.AuthResponse;
import com.ro.auth.dto.mappers.UserDtoMapper;
import com.ro.auth.dto.objects.UserDto;
import com.ro.auth.exception.UserAlreadyExistException;
import com.ro.auth.model.User;
import com.ro.auth.service.AuthService;
import com.ro.auth.service.JwtUserDetailsService;
import com.ro.auth.utils.JwtTokenUtil;
import com.ro.core.ApiError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    return new AuthResponse(accessToken, UserDtoMapper.INSTANCE.toDto(user));
  }

  @PutMapping("/user")
  public UserDto updateInfo(@RequestBody UserDto userDto, Authentication authentication) {
      User user = (User) authentication.getPrincipal();
    User updatedUser = authService.updateInfo(user, userDto);
    return UserDtoMapper.INSTANCE.toDto(updatedUser);
  }

  @PostMapping("/registration")
  public UserDto register(@RequestBody UserDto userDto) {
    User user = authService.register(userDto);
    return UserDtoMapper.INSTANCE.toDto(user);
  }

  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @ExceptionHandler({UserAlreadyExistException.class})
  public ApiError handleUserAlreadyExists(UserAlreadyExistException ex) {
    return new ApiError("Пользователь с указанным адресом эл. почты или номером телефона уже существует");
  }

  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @ExceptionHandler({BadCredentialsException.class})
  public ApiError handleBadCredentials(BadCredentialsException ex) {
    return new ApiError("Неверный адрес эл. почты (номер телефона) или пароль");
  }
}
