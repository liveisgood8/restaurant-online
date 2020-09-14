package com.ro.menu.service;

import com.ro.menu.model.Dish;
import com.ro.menu.model.DishLikes;
import com.ro.menu.model.DishWithImageUrl;
import com.ro.menu.repository.DishLikesRepository;
import com.ro.menu.repository.DishRepository;
import com.ro.core.utils.NullAwareBeanUtilsBean;
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
  private final Path UPLOAD_IMAGES_DIR;
  private final DishRepository dishRepository;
  private final DishLikesRepository dishLikesRepository;

  @Autowired
  public DishService(@Value("${uploads.directory:uploads}") String uploadsDirectory,
                     DishRepository dishRepository,
                     DishLikesRepository dishLikesRepository) throws IOException {
    UPLOAD_IMAGES_DIR = Paths.get(uploadsDirectory, "dish-images");
    this.dishRepository = dishRepository;
    this.dishLikesRepository = dishLikesRepository;
    Files.createDirectories(UPLOAD_IMAGES_DIR);
  }

  public List<DishWithImageUrl> getAll() {
    List<Dish> dishes = dishRepository.findAll();
    return getDishesWithImageUrl(dishes);
  }

  public List<DishWithImageUrl> getByCategoryId(Long categoryId) {
    List<Dish> dishes = dishRepository.findByCategoryId(categoryId);
    return getDishesWithImageUrl(dishes);
  }

  private List<DishWithImageUrl> getDishesWithImageUrl(List<Dish> dishes) {
    return dishes.stream()
        .map(x -> new DishWithImageUrl(x,
            x.getImagePath() != null ? String.format("/menu/dishes/%s/image", x.getId()) : null))
        .collect(Collectors.toList());
  }

  @Transactional
  public Dish create(Dish dish) {
    if (dish.getLikes() == null) {
      DishLikes dishLikes = new DishLikes();
      dishLikes.setDish(dish);
      dishLikes.setLikeCount(0);
      dishLikes.setDislikeCount(0);

      dishLikes = dishLikesRepository.save(dishLikes);
      dish.setLikes(dishLikes);
    }
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
    String filePath = UPLOAD_IMAGES_DIR.resolve(UUID.randomUUID().toString() + ".png")
        .toString();

    BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
    ImageIO.write(bufferedImage, "png", new File(filePath));

    String imagePathString = dishRepository.findImagePathById(dishId);
    if (imagePathString != null) {
      Path imagePath = Paths.get(imagePathString);
      if (Files.exists(imagePath)) {
        Files.delete(imagePath);
      }
    }

    dishRepository.updateImagePath(dishId, filePath);
  }

  public Optional<DishLikes> getLikes(Long dishId) {
    return dishLikesRepository.findByDishId(dishId);
  }

  @Transactional
  public void setLike(Long dishId) {
    Optional<Dish> dish = dishRepository.findById(dishId);
    if (dish.isEmpty()) {
      throw new EntityNotFoundException("Dish with id: " + dishId + " not found");
    }

    DishLikes likes = dish.get().getLikes();
    if (likes == null) {
      likes = new DishLikes();
      likes.setDish(dish.get());
      likes.setLikeCount(1);
      likes.setDislikeCount(0);
    } else {
      likes.setLikeCount(likes.getLikeCount() + 1);
    }

    dishRepository.save(dish.get());
  }

  @Transactional
  public void setDislike(Long dishId) {
    Optional<Dish> dish = dishRepository.findById(dishId);
    if (dish.isEmpty()) {
      throw new EntityNotFoundException("Dish with id: " + dishId + " not found");
    }

    DishLikes likes = dish.get().getLikes();
    if (likes == null) {
      likes = new DishLikes();
      likes.setDish(dish.get());
      likes.setLikeCount(0);
      likes.setDislikeCount(1);
    } else {
      likes.setDislikeCount(likes.getDislikeCount() + 1);
    }

    dishRepository.save(dish.get());
  }
}
