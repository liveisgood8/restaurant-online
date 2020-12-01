package com.ro.auth.data.repository;

import com.ro.auth.data.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  @EntityGraph(attributePaths = {"authorities", "authProvider", "bonusesBalance"})
  Optional<User> findByTelephoneNumberCountryCodeAndTelephoneNumberNationalNumber(String telephoneCountryCode,
                                                                                  String telephoneNationalNumber);

  @EntityGraph(attributePaths = {"authorities", "authProvider", "bonusesBalance"})
  Optional<User> findByEmail(String email);

  boolean existsByEmailOrTelephoneNumberCountryCodeAndTelephoneNumberNationalNumber(String email,
                                                                                    String telephoneCountryCode,
                                                                                    String telephoneNationalNumber);
}
