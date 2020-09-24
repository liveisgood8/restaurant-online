package com.ro.auth.repository;

import com.ro.auth.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Entity;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  @EntityGraph(attributePaths = {"authorities"})
  Optional<User> findByEmailOrPhone(String email, String phone);
}
