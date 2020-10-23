package com.ro.auth.repository;

import com.ro.auth.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  @EntityGraph(attributePaths = {"authorities"})
  Optional<User> findByTelephoneNumberCountryCodeAndTelephoneNumberNationalNumber(String telephoneCountryCode,
                                                                                  String telephoneNationalNumber);

  @EntityGraph(attributePaths = {"authorities"})
  Optional<User> findByEmail(String email);

  @Modifying
  @Query("update User u set u.bonuses = ?2 where u.id = ?1")
  void updateBonuses(Long id, Integer bonuses);

  boolean existsByEmailOrTelephoneNumberCountryCodeAndTelephoneNumberNationalNumber(String email,
                                                                                    String telephoneCountryCode,
                                                                                    String telephoneNationalNumber);
}
