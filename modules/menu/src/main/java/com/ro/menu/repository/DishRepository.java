package com.ro.menu.repository;

import com.ro.menu.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
  List<Dish> findByCategoryId(Long categoryId);

  @Query("select dish.imagePath from Dish dish where dish.id = :id")
  String findImagePathById(Long id);

  @Transactional
  @Modifying
  @Query("update Dish dish set dish.imagePath = :imagePath where dish.id = :id")
  void updateImagePath(Long id, String imagePath);
}
