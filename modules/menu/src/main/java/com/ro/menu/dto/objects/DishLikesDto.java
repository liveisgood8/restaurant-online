package com.ro.menu.dto.objects;

import com.ro.menu.model.DishEmotion;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;

@AllArgsConstructor
@Data
public class DishLikesDto {
  private Integer likeCount;
  private Integer dislikeCount;

  public static DishLikesDto fromEmotions(Collection<DishEmotion> emotions) {
    int likeCounter = 0;
    int dislikeCounter = 0;
    for (DishEmotion e : emotions) {
      if (e.getEmotionType() == DishEmotion.EmotionType.LIKE) {
        likeCounter++;
      } else {
        dislikeCounter++;
      }
    }
    return new DishLikesDto(likeCounter, dislikeCounter);
  }
}
