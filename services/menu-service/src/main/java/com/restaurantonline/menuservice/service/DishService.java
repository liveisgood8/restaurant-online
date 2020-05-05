package com.restaurantonline.menuservice.service;

import com.restaurantonline.menuservice.model.Dish;
import com.restaurantonline.menuservice.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishService {
  @Autowired
  private DishRepository dishRepository;

  public List<Dish> getAll() {
    return dishRepository.findAll();
  }

  public Dish create(Dish dish) {
    return dishRepository.save(dish);
  }

  public void update(Long id, Dish dish) {
    dish.setId(id);
    dishRepository.save(dish);
  }

  public void delete(Long id) {
    dishRepository.deleteById(id);
  }
}
