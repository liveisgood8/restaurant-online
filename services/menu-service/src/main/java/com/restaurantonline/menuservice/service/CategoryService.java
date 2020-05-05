package com.restaurantonline.menuservice.service;

import com.restaurantonline.menuservice.model.Category;
import com.restaurantonline.menuservice.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
  @Autowired
  private CategoryRepository categoryRepository;

  public List<Category> getAll() {
    return categoryRepository.findAll();
  }

  public Category create(Category category) {
    return categoryRepository.save(category);
  }

  public void delete(Long id) {
    categoryRepository.deleteById(id);
  }
}
