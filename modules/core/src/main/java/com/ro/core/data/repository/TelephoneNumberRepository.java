package com.ro.core.data.repository;

import com.ro.core.data.model.TelephoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TelephoneNumberRepository extends JpaRepository<TelephoneNumber, Long> {
  Optional<TelephoneNumber> findByCountryCodeAndNationalNumber(String countryCode, String nationalNumber);
}
