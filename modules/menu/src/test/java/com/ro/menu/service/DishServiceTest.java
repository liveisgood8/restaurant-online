package com.ro.menu.service;

import com.ro.core.CoreTestUtils;
import com.ro.menu.model.Dish;
import com.ro.menu.repository.DishEmotionRepository;
import com.ro.menu.repository.DishRepository;
import net.bytebuddy.implementation.bytecode.Addition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class DishServiceTest {
  @Mock
  private DishRepository dishRepository;

  @Mock
  private DishEmotionRepository dishEmotionRepository;

  private DishService dishService;

  @BeforeEach
  void setUp() throws IOException {
    dishService = new DishService("test", dishRepository, dishEmotionRepository);
  }

  @Test
  void getByName() {
    List<Dish> givenDishes = mockDishRepositoryFindByName("test_contain");

    List<Dish> actual = dishService.getByNameContaining("test_contain");

    assertEquals(givenDishes, actual);
  }

  private List<Dish> mockDishRepositoryFindByName(String nameContain) {
    List<Dish> dishes = CoreTestUtils.getRandomObjectsList(2, Dish.class);

    Mockito.when(dishRepository
        .findByNameContainingIgnoreCase(nameContain))
        .thenReturn(dishes);

    return dishes;
  }
}