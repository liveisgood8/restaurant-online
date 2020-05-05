package com.restaurantonline.authservice.utils;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.ClientRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.List;

@Component
public class KeyCloakInitializer {
  @Autowired
  private Keycloak keycloak;

  @Value("${keycloak.realm}")
  private String realm;

  @EventListener(ApplicationReadyEvent.class)
  public void doSomethingAfterStartup() {
    System.out.println("Инициализация клиентов KeyCloak...");
    createPublicClientIfNotExist();
    createPrivateClientIfNotExist();
    System.out.println("Инициализация клиентов KeyCloak завершена");
  }

  private void createPublicClientIfNotExist() {
    final List<ClientRepresentation> publicClients = keycloak.realm(realm).clients().findByClientId("public-client");
    if (publicClients.isEmpty()) {
      ClientRepresentation client = new ClientRepresentation();
      client.setClientId("public-client");
      client.setPublicClient(true);
      client.setImplicitFlowEnabled(false);
      client.setStandardFlowEnabled(false);
      client.setDirectAccessGrantsEnabled(true);

      Response response = keycloak.realm(realm).clients().create(client);
      if (response.getStatus() != 201) {
        throw new RuntimeException("Не удалось создать KeyCloak клиента: public-client");
      }
    }
  }

  private void createPrivateClientIfNotExist() {
    final List<ClientRepresentation> publicClients = keycloak.realm(realm).clients().findByClientId("private-client");
    if (publicClients.isEmpty()) {
      ClientRepresentation client = new ClientRepresentation();
      client.setClientId("private-client");
      client.setBearerOnly(true);

      Response response = keycloak.realm(realm).clients().create(client);
      if (response.getStatus() != 201) {
        throw new RuntimeException("Не удалось создать KeyCloak клиента: private-client");
      }
    }
  }
}
