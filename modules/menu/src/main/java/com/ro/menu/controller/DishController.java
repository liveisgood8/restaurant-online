package com.ro.menu.controller;

import com.ro.menu.model.Dish;
import com.ro.menu.model.DishWithImageUrl;
import com.ro.menu.service.DishService;
import com.ro.menu.validation.InsertGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.groups.Default;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/menu/dishes")
public class DishController {
  @Autowired
  private DishService dishService;

  @GetMapping
  public List<DishWithImageUrl> getAll(@RequestParam Long categoryId) {
    if (categoryId == null) {
      return dishService.getAll();
    } else {
      return dishService.getByCategoryId(categoryId);
    }
  }

  @GetMapping(value = "{id}/image", produces = MediaType.IMAGE_PNG_VALUE)
  public ResponseEntity<byte[]> getImage(@PathVariable Long id) throws IOException {
    try {
      byte[] imageBytes = dishService.getImageBytes(id);
      return ResponseEntity
          .ok(imageBytes);
    } catch (EntityNotFoundException ignored) {
      return ResponseEntity
          .notFound()
          .build();
    }
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Dish create(@Validated({Default.class, InsertGroup.class}) @RequestBody Dish dish) {
    return dishService.create(dish);
  }

  @PostMapping("{id}/image")
  public void uploadImage(@PathVariable Long id,
                          @RequestParam("file") MultipartFile file) throws IOException {
    dishService.saveImage(id, file);
  }

  @PatchMapping("{id}")
  public void update(@PathVariable Long id, @RequestBody Dish dish) throws Exception {
    dishService.update(id, dish);
  }

  @DeleteMapping("{id}")
  public void delete(@PathVariable Long id) {
    dishService.delete(id);
  }
}
