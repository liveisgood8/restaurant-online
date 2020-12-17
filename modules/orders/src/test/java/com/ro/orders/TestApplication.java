package com.ro.orders;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

@SpringBootApplication
public class TestApplication {
  @MockBean
  private ClientRegistrationRepository registrationRepository;
}
