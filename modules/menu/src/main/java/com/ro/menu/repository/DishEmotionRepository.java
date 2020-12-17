package com.ro.menu.repository;

import com.ro.menu.model.DishEmotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DishEmotionRepository extends JpaRepository<DishEmotion, Long> {
  List<DishEmotion> findByDishId(Long dishId);
  Optional<DishEmotion> findByEmotionTypeAndDishIdAndUserId(DishEmotion.EmotionType emotionType,
                                                            Long dishId, Long userId);
}
