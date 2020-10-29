package com.ro.menu.dto.objects;

import com.ro.menu.model.DishEmotion;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;

@Data
public class DishDto {
  @Data
  public static class LikesDto {
    private int likeCount = 0;
    private int dislikeCount = 0;

    public LikesDto(Collection<DishEmotion> emotions) {
      for (DishEmotion e : emotions) {
        if (e.getEmotionType() == DishEmotion.EmotionType.LIKE) {
          likeCount++;
        } else {
          dislikeCount++;
        }
      }
    }
  }

  private Long id;
  private String name;
  private String description;
  private Double protein;
  private Double fat;
  private Double carbohydrates;
  private Short weight;
  private Short price;
  private String imageUrl;
  private LikesDto likes;
}
