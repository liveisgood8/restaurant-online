package com.ro.menu.repository;

import com.ro.menu.model.Dish;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
  @EntityGraph(attributePaths = {"emotions"})
  @Override
  List<Dish> findAll();

  @EntityGraph(attributePaths = {"emotions"})
  List<Dish> findByCategoryId(Long categoryId);

  @Query("select dish.imagePath from Dish dish where dish.id = ?1")
  String findImagePathById(Long id);

  @Transactional
  @Modifying
  @Query("update Dish dish set dish.imagePath = ?2 where dish.id = ?1")
  void updateImagePath(Long id, String imagePath);
}
