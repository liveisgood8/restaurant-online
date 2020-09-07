package com.ro.menu.controller;

import com.ro.menu.model.Category;
import com.ro.menu.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/menu/categories")
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

  @PatchMapping("{id}")
  public void update(@PathVariable Long id, @RequestBody Category category) throws Exception {
    categoryService.update(id, category);
  }

  @DeleteMapping("{id}")
  public void delete(@PathVariable Long id) {
    categoryService.delete(id);
  }
}
