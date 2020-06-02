package com.restaurantonline.authservice;

import com.restaurantonline.authservice.config.SecurityUnitTestConfig;
import com.restaurantonline.authservice.utils.KeyCloakInitializer;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.Keycloak;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("no-auth")
@Import(SecurityUnitTestConfig.class)
@SpringBootTest
class AuthServiceApplicationTests {
	@MockBean
	private KeyCloakInitializer keyCloakInitializer;

	@MockBean
	private Keycloak keycloak;

	@Test
	void contextLoads() {
	}

}
