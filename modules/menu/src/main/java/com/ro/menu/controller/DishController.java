package com.ro.menu.controller;

import com.ro.auth.data.model.User;
import com.ro.menu.controller.payload.UploadImageResponse;
import com.ro.menu.dto.mappers.DishDtoMapper;
import com.ro.menu.dto.objects.DishDto;
import com.ro.menu.exceptions.EmotionAlreadyExistException;
import com.ro.menu.model.Dish;
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
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/menu/dishes")
public class DishController {
  private final DishService dishService;
  private final DishDtoMapper dishDtoMapper;

  @Autowired
  public DishController(DishService dishService,
                        DishDtoMapper dishDtoMapper) {
    this.dishService = dishService;
    this.dishDtoMapper = dishDtoMapper;
  }

  @GetMapping
  public List<DishDto> getAll(@RequestParam Long categoryId) {
    List<Dish> dishes = categoryId == null ? dishService.getAll() : dishService.getByCategoryId(categoryId);
    return dishDtoMapper.toDto(dishes);
  }

  @GetMapping("/search")
  public List<DishDto> search(@RequestParam(required = false) String name) {
    if (name == null) {
      return Collections.emptyList();
    }

    List<Dish> dishes = dishService.getByNameContaining(name);
    return dishDtoMapper.toDto(dishes);
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
  public UploadImageResponse uploadImage(@PathVariable Long id,
                                         @RequestParam("file") MultipartFile file) throws IOException {
    String imageUrl = dishService.saveImage(id, file);
    return new UploadImageResponse(imageUrl);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public DishDto create(@Validated({Default.class, InsertGroup.class}) @RequestBody Dish dish) {
    Dish createdDish = dishService.create(dish);
    return dishDtoMapper.toDto(createdDish);
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
  public DishDto update(@PathVariable Long id, @RequestBody Dish dish) throws Exception {
    Dish updatedDish = dishService.update(id, dish);
    return dishDtoMapper.toDto(updatedDish);
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
