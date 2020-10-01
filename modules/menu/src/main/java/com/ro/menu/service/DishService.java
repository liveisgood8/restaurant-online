package com.ro.menu.service;

import com.ro.auth.model.User;
import com.ro.menu.exceptions.EmotionAlreadyExistException;
import com.ro.menu.model.Dish;
import com.ro.menu.model.DishEmotion;
import com.ro.menu.model.DishWithImageUrlAndLikes;
import com.ro.menu.repository.DishLikesRepository;
import com.ro.menu.repository.DishRepository;
import com.ro.core.utils.NullAwareBeanUtilsBean;
import com.ro.menu.utils.FileUploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DishService {
  private final Path uploadImagesDir;
  private final DishRepository dishRepository;
  private final DishLikesRepository dishEmotionsRepository;

  @Autowired
  public DishService(@Value("${uploads.directory:uploads}") String uploadsDirectory,
                     DishRepository dishRepository,
                     DishLikesRepository dishEmotionsRepository) throws IOException {
    uploadImagesDir = Paths.get(uploadsDirectory, "dish-images");
    this.dishRepository = dishRepository;
    this.dishEmotionsRepository = dishEmotionsRepository;
    Files.createDirectories(uploadImagesDir);
  }

  public List<DishWithImageUrlAndLikes> getAll() {
    List<Dish> dishes = dishRepository.findAll();
    return getDishesWithImageUrl(dishes);
  }

  public List<DishWithImageUrlAndLikes> getByCategoryId(Long categoryId) {
    List<Dish> dishes = dishRepository.findByCategoryId(categoryId);
    return getDishesWithImageUrl(dishes);
  }

  private List<DishWithImageUrlAndLikes> getDishesWithImageUrl(List<Dish> dishes) {
    return dishes.stream()
        .map(x -> new DishWithImageUrlAndLikes(x,
            x.getImagePath() != null ? String.format("/menu/dishes/%s/image", x.getId()) : null))
        .collect(Collectors.toList());
  }

  @Transactional
  public Dish create(Dish dish) {
    return dishRepository.save(dish);
  }

  @Transactional
  public void update(Long id, Dish newDish) throws Exception {
    Dish dish = dishRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    NullAwareBeanUtilsBean.copyNonNullProperties(newDish, dish);
    dishRepository.save(dish);
  }

  public void delete(Long id) {
    dishRepository.deleteById(id);
  }

  public byte[] getImageBytes(Long dishId) throws IOException, EntityNotFoundException {
    String imagePath = dishRepository.findImagePathById(dishId);
    if (imagePath == null || !Files.exists(Paths.get(imagePath))) {
      throw new EntityNotFoundException();
    }

    return Files.readAllBytes(Paths.get(imagePath));
  }

  public void saveImage(Long dishId, MultipartFile file) throws IOException {
    String newImagePath = FileUploadUtils.saveUploadedFile(uploadImagesDir, file,
        "png", "jpg", "jpeg", "svg");

    String imagePathString = dishRepository.findImagePathById(dishId);
    if (imagePathString != null) {
      FileUploadUtils.deleteUploadedFile(imagePathString);
    }

    dishRepository.updateImagePath(dishId, newImagePath);
  }

  public List<DishEmotion> getLikes(Long dishId) {
    return dishEmotionsRepository.findByDishId(dishId);
  }

  @Transactional
  public void setLike(Long dishId, User user)  {
    createDishEmotion(DishEmotion.EmotionType.LIKE, dishId, user);
  }

  @Transactional
  public void setDislike(Long dishId, User user) {
    createDishEmotion(DishEmotion.EmotionType.DISLIKE, dishId, user);
  }

  @Transactional
  public void createDishEmotion(DishEmotion.EmotionType type, Long dishId, User user) {
    Optional<Dish> dish = dishRepository.findById(dishId);
    if (dish.isEmpty()) {
      throw new EntityNotFoundException("Dish with id: " + dishId + " not found");
    }

    Optional<DishEmotion> emotion = dishEmotionsRepository.findByEmotionTypeAndDishIdAndUserId(type,
        dishId, user.getId());
    if (emotion.isPresent()) {
      throw new EmotionAlreadyExistException(type);
    }

    DishEmotion newEmotion = new DishEmotion();
    newEmotion.setDish(dish.get());
    newEmotion.setEmotionType(type);
    newEmotion.setUser(user);

    dishEmotionsRepository.save(newEmotion);
  }
}
