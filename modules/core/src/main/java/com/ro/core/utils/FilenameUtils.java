package com.ro.core.utils;

public class FilenameUtils {
  public static String getExtension(String fullFileName) {
    return fullFileName.substring(fullFileName.lastIndexOf(".") + 1);
  }
}
