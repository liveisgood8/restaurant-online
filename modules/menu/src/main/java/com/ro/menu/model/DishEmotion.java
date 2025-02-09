package com.ro.menu.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.ro.auth.data.model.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "dish_emotions", indexes = {
    @Index(name = "user_unique_emotion", columnList = "dish_id,emotion_type,user_id")
})
public class DishEmotion {
  public enum EmotionType {
    LIKE,
    DISLIKE,
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JsonBackReference
  @ManyToOne
  @JoinColumn(name = "dish_id", nullable = false)
  private Dish dish;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "emotion_type", nullable = false)
  private EmotionType emotionType;
}
