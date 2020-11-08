package com.ro.menu.service;

import com.ro.auth.data.model.User;
import com.ro.menu.dto.mappers.ImageMapper;
import com.ro.menu.exceptions.EmotionAlreadyExistException;
import com.ro.menu.model.Dish;
import com.ro.menu.model.DishEmotion;
import com.ro.menu.repository.DishLikesRepository;
import com.ro.menu.repository.DishRepository;
import com.ro.core.utils.NullAwareBeanUtilsBean;
import com.ro.menu.utils.FileUploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

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

  public List<Dish> getAll() {
    return dishRepository.findAll();
  }

  public List<Dish> getByCategoryId(Long categoryId) {
    return dishRepository.findByCategoryId(categoryId);
  }

  @Transactional
  public Dish create(Dish dish) {
    return dishRepository.save(dish);
  }

  // TODO Переделать на DTO
  @Transactional
  public Dish update(Long id, Dish newDish) throws Exception {
    Dish dish = dishRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    NullAwareBeanUtilsBean.copyNonNullProperties(newDish, dish);
    return dishRepository.save(dish);
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

  public String saveImage(Long dishId, MultipartFile file) throws IOException {
    String newImagePath = FileUploadUtils.saveUploadedImageAsPng(uploadImagesDir, file,
        "png", "jpg", "jpeg", "svg");

    String imagePathString = dishRepository.findImagePathById(dishId);
    if (imagePathString != null) {
      FileUploadUtils.deleteUploadedFile(imagePathString);
    }

    dishRepository.updateImagePath(dishId, newImagePath);
    return ImageMapper.makeImageUrl(ImageMapper.ImageSource.DISH, dishId, newImagePath);
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
