package com.ro.menu.dto.mappers;

import com.sun.istack.Nullable;
import org.apache.commons.io.FilenameUtils;

public class ImageMapper {
  public enum ImageSource {
    DISH,
    CATEGORY
  }

  public static String makeImageUrl(ImageSource imageSource, Long id, @Nullable String imagePath) {
    if (imagePath == null) {
      return null;
    }
    String fileNamePart = FilenameUtils.getBaseName(imagePath).substring(0, 15);
    String partUrl = imageSource == ImageSource.DISH ? "dishes" : "categories";
    return String.format("/menu/%s/%s/image?%s", partUrl, id, fileNamePart);
  }
}
