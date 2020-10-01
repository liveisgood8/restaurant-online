package com.ro.menu.utils;

import com.ro.core.utils.FilenameUtils;
import com.sun.istack.NotNull;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUploadUtils {
  public static String saveUploadedFile(Path uploadDirectory, MultipartFile file, String... expectedExtensions)
      throws IOException {
    validateUploadedFileExtension(file, expectedExtensions);

    String filePath = uploadDirectory.resolve(UUID.randomUUID().toString() + ".png")
        .toString();

    BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
    ImageIO.write(bufferedImage, "png", new File(filePath));

    return filePath;
  }

  public static void deleteUploadedFile(@NotNull String path) throws IOException {
    Path filePath = Paths.get(path);
    if (Files.exists(filePath)) {
      Files.delete(filePath);
    }
  }

  private static void validateUploadedFileExtension(MultipartFile file, String... expectedExtensions) {
    if (file.getOriginalFilename() == null) {
      throw new IllegalArgumentException("Не указана оригенальное наименование файла");
    }

    Stream<String> expectedExtensionsStream = Arrays.stream(expectedExtensions);
    String fileNameExtension = FilenameUtils.getExtension(file.getOriginalFilename());
    if (expectedExtensionsStream.noneMatch(fileNameExtension::equals)) {
      throw new IllegalArgumentException("Невалидный формат изображения: " + fileNameExtension +
          ", валидные форматы: " + expectedExtensionsStream.collect(Collectors.joining(",")));
    }
  }
}
