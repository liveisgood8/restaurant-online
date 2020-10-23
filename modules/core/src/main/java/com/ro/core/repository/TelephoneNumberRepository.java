package com.ro.core.repository;

import com.ro.core.model.TelephoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelephoneNumberRepository extends JpaRepository<TelephoneNumber, Long> {
}
