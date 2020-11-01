package com.ro.orders.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ro.auth.model.AuthProvider;
import com.ro.auth.model.User;
import com.ro.auth.repository.AuthProviderRepository;
import com.ro.auth.repository.UserRepository;
import com.ro.core.model.TelephoneNumber;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Random;

public abstract class AbstractControllerTest {
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AuthProviderRepository authProviderRepository;

  @Autowired
  private PlatformTransactionManager platformTransactionManager;
  protected final EasyRandom er = new EasyRandom();
  protected final EasyRandom onlyPositiveEr;

  public AbstractControllerTest() {
    Random random = new Random();
    EasyRandomParameters parameters = new EasyRandomParameters()
        .randomize(Integer.class, () -> Math.abs(random.nextInt()))
        .randomize(Short.class, () -> (short) Math.abs(random.nextInt()));

    onlyPositiveEr = new EasyRandom(parameters);
  }

  protected UserDetails createAndSaveUser() {
    AuthProvider authProvider = er.nextObject(AuthProvider.class);
    authProvider = authProviderRepository.save(authProvider);

    User user = er.nextObject(User.class);
    user.setEmail("test@test.com");
    user.setTelephoneNumber(null);
    user.setAuthProvider(authProvider);

    return userRepository.save(user);
  }

  protected String asJson(Object value) {
    try {
      return objectMapper.writeValueAsString(value);
    } catch (JsonProcessingException ex) {
      throw new RuntimeException(ex.getMessage());
    }
  }

  protected void inTransaction(Runnable runnable) {
    TransactionTemplate transactionTemplate = new TransactionTemplate(platformTransactionManager);
    transactionTemplate.execute(new TransactionCallbackWithoutResult() {
      @Override
      protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
        runnable.run();
      }
    });
  }
}
