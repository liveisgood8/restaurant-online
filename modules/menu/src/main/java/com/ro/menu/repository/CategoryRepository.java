package com.ro.menu.repository;

import com.ro.menu.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  @Query("select c.imagePath from Category c where c.id = :id")
  String findImagePathById(Long id);

  @Modifying
  @Query("update Category c set c.imagePath = :imagePath where c.id = :id")
  void updateImagePath(Long id, String imagePath);
}
