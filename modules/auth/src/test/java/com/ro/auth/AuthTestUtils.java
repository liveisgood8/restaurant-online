package com.ro.auth;

import com.ro.auth.model.AuthProvider;
import com.ro.auth.model.User;
import com.ro.auth.repository.AuthProviderRepository;
import com.ro.auth.repository.UserRepository;
import com.ro.core.CoreTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthTestUtils {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AuthProviderRepository authProviderRepository;

  public UserDetails createAndSaveUserInDataSource() {
    User user = CoreTestUtils.getRandomObject(User.class);
    user.setEmail("test@test.com");
    user.setTelephoneNumber(null);
    user.setAuthProvider(authProviderRepository.findByName(AuthProvider.NATIVE).orElseThrow());

    return userRepository.save(user);
  }
}
