package com.ro.menu.controller.payload;

import com.ro.menu.model.DishLikes;

public class DishLikesResponse {
  public final Integer likeCount;
  public final Integer dislikeCount;

  public DishLikesResponse(Integer likeCount, Integer dislikeCount) {
    this.likeCount = likeCount;
    this.dislikeCount = dislikeCount;
  }

  public DishLikesResponse(DishLikes dishLikes) {
    this.likeCount = dishLikes.getLikeCount();
    this.dislikeCount = dishLikes.getDislikeCount();
  }

  public static DishLikesResponse createEmpty() {
    return new DishLikesResponse(0, 0);
  }
}
