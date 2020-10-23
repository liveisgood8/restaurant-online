package com.ro.auth.service;

import com.ro.auth.model.User;
import com.ro.auth.repository.UserRepository;
import com.ro.core.model.TelephoneNumber;
import com.ro.core.utils.TelephoneNumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JwtUserDetailsService implements UserDetailsService {
  private UserRepository userRepository;

  @Autowired
  public void setUserRepository(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> user;
    try {
      TelephoneNumber telephoneNumber = TelephoneNumberUtils.fromString(username);
      user = userRepository
          .findByTelephoneNumberCountryCodeAndTelephoneNumberNationalNumber(telephoneNumber.getCountryCode(),
              telephoneNumber.getNationalNumber());
    } catch (RuntimeException ignore) {
      user = userRepository.findByEmail(username);
    }
    if (user.isEmpty()) {
      throw new UsernameNotFoundException("User with email/phone: " + username + " not found");
    }

    return user.get();
  }

}
