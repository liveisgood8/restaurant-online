package com.restaurantonline.menuservice.controller;

import com.restaurantonline.menuservice.model.Dish;
import com.restaurantonline.menuservice.service.DishService;
import com.restaurantonline.menuservice.validation.InsertGroup;
import com.restaurantonline.menuservice.validation.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.List;

@RestController
@RequestMapping("/dishes")
public class DishController {
  @Autowired
  private DishService dishService;

  @GetMapping
  public List<Dish> getAll() {
    return dishService.getAll();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void create(@Validated({Default.class, InsertGroup.class}) @RequestBody Dish dish) {
    dishService.create(dish);
  }

  @PutMapping("{id}")
  public void update(@PathVariable Long id, @Validated({Default.class, UpdateGroup.class}) @RequestBody Dish dish) {
    dishService.update(id, dish);
  }

  @DeleteMapping("{id}")
  public void delete(@PathVariable Long id) {
    dishService.delete(id);
  }
}
