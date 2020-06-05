package com.restaurantonline.menuservice.service;

import com.restaurantonline.menuservice.model.Dish;
import com.restaurantonline.menuservice.model.DishWithImageUrl;
import com.restaurantonline.menuservice.repository.DishRepository;
import com.restaurantonline.menuservice.utils.NullAwareBeanUtilsBean;
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
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DishService {
  private final Path UPLOAD_IMAGES_DIR;

  @Autowired
  private DishRepository dishRepository;

  @Autowired
  public DishService(@Value("${uploads.directory:uploads}") String uploadsDirectory) throws IOException {
    UPLOAD_IMAGES_DIR = Paths.get(uploadsDirectory, "dish-images");
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
            String.format("/menu/dishes/%s/image", x.getId())))
        .collect(Collectors.toList());
  }

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
}
