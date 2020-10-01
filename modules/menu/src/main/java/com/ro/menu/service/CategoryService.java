package com.ro.menu.service;

import com.ro.menu.model.Category;
import com.ro.menu.repository.CategoryRepository;
import com.ro.core.utils.NullAwareBeanUtilsBean;
import com.ro.menu.utils.FileUploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
  private final Path uploadImagesDir;
  private final CategoryRepository categoryRepository;

  @Autowired
  public CategoryService(@Value("${uploads.directory:uploads}") String uploadsDirectory,
                         CategoryRepository categoryRepository) throws IOException {
    this.uploadImagesDir = Paths.get(uploadsDirectory, "category-images");
    this.categoryRepository = categoryRepository;
    Files.createDirectories(uploadImagesDir);
  }

  public List<Category> getAll() {
    return categoryRepository.findAll()
        .stream()
        .peek(c -> c.setImageUrl(String.format("/menu/categories/%s/image", c.getId())))
        .collect(Collectors.toList());
  }

  public byte[] getImageBytes(Long categoryId) throws IOException, EntityNotFoundException {
    String imagePath = categoryRepository.findImagePathById(categoryId);
    if (imagePath == null || !Files.exists(Paths.get(imagePath))) {
      throw new EntityNotFoundException("Изображение категории не найдено, идентификатор: " + categoryId);
    }

    return Files.readAllBytes(Paths.get(imagePath));
  }

  @Modifying
  @Transactional
  public void saveImage(Long categoryId, MultipartFile file) throws IOException {
    String newImagePath = FileUploadUtils.saveUploadedImageAsPng(uploadImagesDir, file,
        "png", "jpg", "jpeg", "svg");

    String imagePathString = categoryRepository.findImagePathById(categoryId);
    if (imagePathString != null) {
      FileUploadUtils.deleteUploadedFile(imagePathString);
    }

    categoryRepository.updateImagePath(categoryId, newImagePath);
  }

  @Transactional
  public Category create(Category category) {
    return categoryRepository.save(category);
  }

  @Transactional
  public void update(Long id, Category newCategory) throws Exception {
    Category category = categoryRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    NullAwareBeanUtilsBean.copyNonNullProperties(newCategory, category);
    categoryRepository.save(category);
  }

  public void delete(Long id) {
    categoryRepository.deleteById(id);
  }
}
