package com.ro.menu.repository;

import com.ro.menu.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  @Override
  @Query("select c from Category c where c.archived = false")
  List<Category> findAll();

  @Query("select c.imagePath from Category c where c.id = ?1")
  String findImagePathById(Long id);

  @Modifying
  @Query("update Category c set c.imagePath = ?2 where c.id = ?1")
  void updateImagePath(Long id, String imagePath);

  @Modifying
  @Query("update Category c set c.archived = ?2 where c.id = ?1")
  void updateArchived(Long id, Boolean archived);
}
