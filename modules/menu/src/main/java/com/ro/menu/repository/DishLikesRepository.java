package com.ro.menu.repository;

import com.ro.menu.model.DishLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DishLikesRepository extends JpaRepository<DishLikes, Long> {
  Optional<DishLikes> findByDishId(Long dishId);
}
