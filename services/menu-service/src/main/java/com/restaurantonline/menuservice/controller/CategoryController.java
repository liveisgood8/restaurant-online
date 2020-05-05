package com.restaurantonline.menuservice.controller;

import com.restaurantonline.menuservice.model.Category;
import com.restaurantonline.menuservice.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
  @Autowired
  private CategoryService categoryService;

  @GetMapping
  public List<Category> getAll() {
    return categoryService.getAll();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Category create(@Valid @RequestBody Category category) {
    return categoryService.create(category);
  }

  @DeleteMapping("{id}")
  public void delete(@PathVariable Long id) {
    categoryService.delete(id);
  }
}
