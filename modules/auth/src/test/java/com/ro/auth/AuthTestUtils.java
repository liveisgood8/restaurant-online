package com.ro.auth;

import com.ro.auth.data.model.AuthProvider;
import com.ro.auth.data.model.User;
import com.ro.auth.data.repository.AuthProviderRepository;
import com.ro.auth.data.repository.UserRepository;
import com.ro.core.CoreTestUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AuthTestUtils {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AuthProviderRepository authProviderRepository;

  public User createAndSaveUserInDataSource() {
    User user = CoreTestUtils.getRandomObject(User.class);
    user.setEmail("test@test.com");
    user.setTelephoneNumber(null);
    user.setAuthProvider(authProviderRepository.findByName(AuthProvider.NATIVE).orElseThrow());

    return userRepository.save(user);
  }

  @Transactional
  public User getInitializedUser(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow();
  }
}
