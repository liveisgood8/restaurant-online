package com.ro.auth.controller;

import com.ro.auth.controller.body.AuthRequest;
import com.ro.auth.controller.body.AuthResponse;
import com.ro.auth.data.dto.mappers.UserDtoMapper;
import com.ro.auth.data.dto.objects.UserDto;
import com.ro.auth.data.lib.AuthenticateData;
import com.ro.auth.exception.UserAlreadyExistException;
import com.ro.auth.data.model.User;
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
  private final UserDtoMapper userDtoMapper;

  @Autowired
  public AuthController(AuthService authService,
                        UserDtoMapper userDtoMapper) {
    this.authService = authService;
    this.userDtoMapper = userDtoMapper;
  }

  @PostMapping
  public AuthResponse authenticate(@RequestBody AuthRequest authRequest) {
    AuthenticateData authenticateData = authService.authenticateUser(authRequest.getLogin(), authRequest.getPassword());
    return new AuthResponse(userDtoMapper.toDto(authenticateData.getUser()), authenticateData.getAccessToken());
  }

  @PutMapping("/user")
  public UserDto update(@RequestBody UserDto userDto, Authentication authentication) {
    User authenticatedUser = (User) authentication.getPrincipal();
    if (!userDto.getId().equals(authenticatedUser.getId())) {
      throw new IllegalArgumentException("Could not updated another user info");
    }

    User user = userDtoMapper.toEntity(userDto);
    user = authService.update(user);

    return userDtoMapper.toDto(user);
  }

  @PostMapping("/registration")
  public UserDto register(@RequestBody UserDto userDto) {
    User user = userDtoMapper.toEntity(userDto);
    user = authService.register(user);
    return userDtoMapper.toDto(user);
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
