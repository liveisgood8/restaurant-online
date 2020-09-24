package com.ro.menu.exceptions;

import com.ro.menu.model.DishEmotion;

public class EmotionAlreadyExistException extends RuntimeException {
  public EmotionAlreadyExistException(DishEmotion.EmotionType type) {
    super("Вы уже ставили " + (type == DishEmotion.EmotionType.LIKE ? "лайк" : "дизлайк") + " данному блюду");
  }
}
