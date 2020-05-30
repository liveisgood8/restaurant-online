package com.restaurantonline.menuservice.controller;

import com.restaurantonline.menuservice.model.Dish;
import com.restaurantonline.menuservice.service.DishService;
import com.restaurantonline.menuservice.validation.InsertGroup;
import com.restaurantonline.menuservice.validation.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.groups.Default;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/dishes")
public class DishController {
  @Autowired
  private DishService dishService;

  @GetMapping
  public List<Dish> getAll(@RequestParam Long categoryId) {
    if (categoryId == null) {
      return dishService.getAll();
    } else {
      return dishService.getByCategoryId(categoryId);
    }
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Dish create(@Validated({Default.class, InsertGroup.class}) @RequestBody Dish dish) {
    return dishService.create(dish);
  }

  @PostMapping("/upload-image")
  public String uploadDishImage(@RequestParam("file") MultipartFile file) throws IOException {
    return dishService.saveImageInPublicDirectoryAndGetUrl(file);
  }

  @PutMapping("{id}")
  public void update(@PathVariable Long id, @Validated({Default.class, UpdateGroup.class}) @RequestBody Dish dish) {
    dishService.update(id, dish);
  }

  @DeleteMapping("{id}")
  public void delete(@PathVariable Long id) {
    dishService.delete(id);
  }
}
