package com.ro.menu.repository;

import com.ro.menu.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  @Query("select c.imagePath from Category c where c.id = ?1")
  String findImagePathById(Long id);

  @Modifying
  @Query("update Category c set c.imagePath = ?2 where c.id = ?1")
  void updateImagePath(Long id, String imagePath);
}
