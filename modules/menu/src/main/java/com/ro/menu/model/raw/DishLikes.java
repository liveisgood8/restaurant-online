package com.ro.menu.model.raw;

import com.ro.menu.model.DishEmotion;

import java.util.Collection;
import java.util.List;

public class DishLikes {
  public final Integer likeCount;
  public final Integer dislikeCount;

  public DishLikes(Integer likeCount, Integer dislikeCount) {
    this.likeCount = likeCount;
    this.dislikeCount = dislikeCount;
  }

  public DishLikes(Collection<DishEmotion> dishEmotions) {
    int likeCounter =  0;
    int dislikeCounter = 0;
    for (DishEmotion dishEmotion : dishEmotions) {
      if (dishEmotion.getEmotionType() == DishEmotion.EmotionType.LIKE) {
        likeCounter++;
      } else {
        dislikeCounter++;
      }
    }
    likeCount = likeCounter;
    dislikeCount = dislikeCounter;
  }

  public static DishLikes createEmpty() {
    return new DishLikes(0, 0);
  }
}
