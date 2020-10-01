package com.ro.menu.controller;

import com.ro.auth.model.User;
import com.ro.menu.model.raw.DishLikes;
import com.ro.menu.exceptions.EmotionAlreadyExistException;
import com.ro.menu.model.Dish;
import com.ro.menu.model.DishEmotion;
import com.ro.menu.model.DishWithImageUrlAndLikes;
import com.ro.menu.service.DishService;
import com.ro.menu.validation.ApiError;
import com.ro.menu.validation.InsertGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
  public List<DishWithImageUrlAndLikes> getAll(@RequestParam Long categoryId) {
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

  @PostMapping("{id}/image")
  public void uploadImage(@PathVariable Long id,
                          @RequestParam("file") MultipartFile file) throws IOException {
    dishService.saveImage(id, file);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Dish create(@Validated({Default.class, InsertGroup.class}) @RequestBody Dish dish) {
    return dishService.create(dish);
  }

  @GetMapping("{id}/likes")
  public DishLikes getLikes(@PathVariable Long id) {
    List<DishEmotion> likes = dishService.getLikes(id);
    return new DishLikes(likes);
  }

  @PostMapping("{id}/likes/like")
  public void setLike(@PathVariable Long id, Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    dishService.setLike(id, user);
  }

  @PostMapping("{id}/likes/dislike")
  public void setDislike(@PathVariable Long id, Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    dishService.setDislike(id, user);
  }

  @PatchMapping("{id}")
  public void update(@PathVariable Long id, @RequestBody Dish dish) throws Exception {
    dishService.update(id, dish);
  }

  @DeleteMapping("{id}")
  public void delete(@PathVariable Long id) {
    dishService.delete(id);
  }

  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @ExceptionHandler({EmotionAlreadyExistException.class})
  public ApiError handleEmotionAlreadyExist(EmotionAlreadyExistException ex) {
    return new ApiError(ex.getMessage());
  }
}
