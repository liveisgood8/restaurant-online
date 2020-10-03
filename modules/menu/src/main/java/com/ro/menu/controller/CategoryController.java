package com.ro.menu.controller;

import com.ro.menu.controller.payload.UploadImageResponse;
import com.ro.menu.model.Category;
import com.ro.menu.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/menu/categories")
public class CategoryController {
  @Autowired
  private CategoryService categoryService;

  @GetMapping(value = "{id}/image", produces = MediaType.IMAGE_PNG_VALUE)
  public ResponseEntity<byte[]> getImage(@PathVariable Long id) throws IOException {
    try {
      byte[] imageBytes = categoryService.getImageBytes(id);
      return ResponseEntity
          .ok(imageBytes);
    } catch (EntityNotFoundException ignored) {
      return ResponseEntity
          .notFound()
          .build();
    }
  }

  @PostMapping("{id}/image")
  public UploadImageResponse uploadImage(@PathVariable Long id,
                                         @RequestParam("file") MultipartFile file) throws IOException {
    String imageUrl = categoryService.saveImage(id, file);
    return new UploadImageResponse(imageUrl);
  }

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
