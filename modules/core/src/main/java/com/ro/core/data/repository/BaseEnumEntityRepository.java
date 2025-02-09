package com.ro.core.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.Optional;

@NoRepositoryBean
public interface BaseEnumEntityRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {
  Optional<T> findByName(String name);
}
