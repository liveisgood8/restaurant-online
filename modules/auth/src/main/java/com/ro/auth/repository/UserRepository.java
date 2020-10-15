package com.ro.auth.repository;

import com.ro.auth.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Entity;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  @EntityGraph(attributePaths = {"authorities"})
  Optional<User> findByTelephoneNumberCountryCodeAndTelephoneNumberNationalNumber(String telephoneCountryCode,
                                                                                  String telephoneNationalNumber);

  @EntityGraph(attributePaths = {"authorities"})
  Optional<User> findByEmail(String email);

  boolean existsByEmailOrTelephoneNumberCountryCodeAndTelephoneNumberNationalNumber(String email,
                                                                                    String telephoneCountryCode,
                                                                                    String telephoneNationalNumber);
}
