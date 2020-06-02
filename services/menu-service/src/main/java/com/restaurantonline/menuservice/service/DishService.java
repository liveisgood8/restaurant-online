package com.restaurantonline.menuservice.service;

import com.restaurantonline.menuservice.model.Dish;
import com.restaurantonline.menuservice.repository.DishRepository;
import com.restaurantonline.menuservice.utils.NullAwareBeanUtilsBean;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class DishService {
  @Autowired
  private DishRepository dishRepository;

  private final Path UPLOAD_IMAGES_DIR =
      Paths.get("/media/ExtPartition/ro/menu-service/dish-images");

  @PostConstruct
  private void createUploadsDirectory() throws IOException {
    Files.createDirectories(UPLOAD_IMAGES_DIR);
  }

  public List<Dish> getAll() {
    return dishRepository.findAll();
  }

  public List<Dish> getByCategoryId(Long categoryId) {
    return dishRepository.findByCategoryId(categoryId);
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

  public String saveImageInPublicDirectoryAndGetUrl(MultipartFile file) throws IOException {
    String fileName = UUID.randomUUID().toString() +
        "." + FilenameUtils.getExtension(file.getOriginalFilename());

    Path destination = UPLOAD_IMAGES_DIR.resolve(fileName);
    file.transferTo(destination);

    return "/dish-images/" + fileName;
  }
}
