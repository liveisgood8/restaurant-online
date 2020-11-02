package com.ro.orders.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ro.auth.model.AuthProvider;
import com.ro.auth.model.User;
import com.ro.auth.repository.AuthProviderRepository;
import com.ro.auth.repository.UserRepository;
import com.ro.orders.utils.ObjectGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;


public abstract class AbstractControllerTest {
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AuthProviderRepository authProviderRepository;

  protected UserDetails createAndSaveUser() {
    User user = ObjectGenerator.getRandomObject(User.class);
    user.setEmail("test@test.com");
    user.setTelephoneNumber(null);
    user.setAuthProvider(authProviderRepository.findByName(AuthProvider.NATIVE).orElseThrow());

    return userRepository.save(user);
  }

  protected String asJson(Object value) {
    try {
      return objectMapper.writeValueAsString(value);
    } catch (JsonProcessingException ex) {
      throw new RuntimeException(ex.getMessage());
    }
  }
}
