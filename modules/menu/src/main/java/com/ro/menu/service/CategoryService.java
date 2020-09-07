package com.ro.menu.service;

import com.ro.menu.model.Category;
import com.ro.menu.repository.CategoryRepository;
import com.ro.core.utils.NullAwareBeanUtilsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
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

  @Transactional
  public void update(Long id, Category newCategory) throws Exception {
    Category category = categoryRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    NullAwareBeanUtilsBean.copyNonNullProperties(newCategory, category);
    categoryRepository.save(category);
  }

  public void delete(Long id) {
    categoryRepository.deleteById(id);
  }
}
