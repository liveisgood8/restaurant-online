package com.restaurantonline.authservice.controllers;

import com.restaurantonline.authservice.models.User;
import com.restaurantonline.authservice.services.AuthService;
import com.restaurantonline.authservice.utils.NotValidDataException;
import com.restaurantonline.authservice.validation.InsertGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private AuthService authService;

  @PostMapping("/register")
  public void register(@Validated({Default.class, InsertGroup.class}) @RequestBody User user)
      throws NotValidDataException {
    authService.register(user);
  }
}
