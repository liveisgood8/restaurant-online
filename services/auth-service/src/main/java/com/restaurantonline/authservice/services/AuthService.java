package com.restaurantonline.authservice.services;

import com.restaurantonline.authservice.models.User;
import com.restaurantonline.authservice.utils.NotValidDataException;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

@Service
public class AuthService {
  @Value("${keycloak.realm}")
  private String keycloakRealm;

  @Autowired
  private Keycloak keycloak;


  public void register(User user) throws NotValidDataException {
    List<UserRepresentation> existUsers = keycloak.realm(keycloakRealm).users().search(user.getLogin());
    if (!existUsers.isEmpty()) {
      throw new NotValidDataException("Пользователь с указанным логином уже существует");
    }

    CredentialRepresentation credential = new CredentialRepresentation();
    credential.setType(CredentialRepresentation.PASSWORD);
    credential.setValue(user.getPassword());
    credential.setTemporary(false);

    UserRepresentation cloakUser = new UserRepresentation();
    cloakUser.setUsername(user.getLogin());
    cloakUser.setCredentials(Collections.singletonList(credential));
    cloakUser.setEnabled(true);

    Response response = keycloak.realm(keycloakRealm).users().create(cloakUser);
    if (response.getStatus() != 201) {
      throw new RuntimeException("Не удалось создать нового пользователя");
    }
  }
}
