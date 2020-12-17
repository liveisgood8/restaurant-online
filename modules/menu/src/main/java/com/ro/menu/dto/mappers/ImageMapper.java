package com.ro.menu.dto.mappers;

import com.sun.istack.Nullable;
import org.apache.commons.io.FilenameUtils;

import java.util.Base64;

public class ImageMapper {
  public enum ImageSource {
    DISH,
    CATEGORY
  }

  public static String makeImageUrl(ImageSource imageSource, Long id, @Nullable String imagePath) {
    if (imagePath == null) {
      return null;
    }
    String fileNameBase64 = Base64.getEncoder().encodeToString(FilenameUtils.getBaseName(imagePath).getBytes());
    String partUrl = imageSource == ImageSource.DISH ? "dishes" : "categories";
    return String.format("/menu/%s/%s/image?%s", partUrl, id, fileNameBase64);
  }
}
