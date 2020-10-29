package com.ro.orders.controller;

import com.ro.auth.config.AuthModuleConfig;
import com.ro.core.CoreModuleConfig;
import com.ro.menu.config.MenuModuleConfig;
import com.ro.menu.service.DishService;
import com.ro.orders.config.OrdersServiceConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class OrdersControllerIntegrationTest {

  @MockBean
  private ClientRegistrationRepository clientRegistrationRepository;

  @Autowired
  private MockMvc mockMvc;

  @Test
  void makeOrder() {
    // TODO Implement
  }

  @SpringBootApplication
  @Import({OrdersServiceConfig.class, MenuModuleConfig.class, CoreModuleConfig.class, AuthModuleConfig.class})
  static class TestApplication {
  }
}