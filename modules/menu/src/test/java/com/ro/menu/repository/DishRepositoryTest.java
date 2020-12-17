package com.ro.menu.repository;

import com.ro.auth.config.AuthModuleConfig;
import com.ro.auth.data.model.User;
import com.ro.core.CoreModuleConfig;
import com.ro.core.CoreTestUtils;
import com.ro.menu.config.MenuModuleConfig;
import com.ro.menu.model.Category;
import com.ro.menu.model.Dish;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@MockBean(ClientRegistrationRepository.class)
@Import({
    MenuModuleConfig.class,
    AuthModuleConfig.class,
    CoreModuleConfig.class
})
class DishRepositoryTest {
  @Autowired
  private CategoryRepository categoryRepository;

  @Autowired
  private DishRepository dishRepository;

  @Test
  void findByNameContainingIgnoreCase() {
    List<Dish> givenDishes = createAndSaveDishes();

    List<Dish> actual = dishRepository.findByNameContainingIgnoreCase(givenDishes.get(1).getName());

    assertEquals(1, actual.size());
    assertDishEqual(givenDishes.get(1), actual.get(0));
  }

  @Test
  void findByNameContainingIgnoreCase_whenPartOfName() {
    List<Dish> givenDishes = createAndSaveDishes();

    String firstDishNameSubstring = givenDishes.get(0).getName()
        .substring(0, givenDishes.get(0).getName().length() - 3);
    List<Dish> actual = dishRepository.findByNameContainingIgnoreCase(firstDishNameSubstring);

    assertEquals(1, actual.size());
    assertDishEqual(givenDishes.get(0), actual.get(0));
  }

  @Transactional
  List<Dish> createAndSaveDishes() {
    final Category category = CoreTestUtils.getRandomObject(Category.class);

    List<Dish> dishes = CoreTestUtils.getRandomObjectsList(4, Dish.class);
    dishes.forEach(d -> d.setCategory(category));
    category.setDishes(dishes);

    Category savedCategory = categoryRepository.save(category);
    savedCategory.getDishes().forEach(d -> Hibernate.initialize(d.getEmotions()));

    return savedCategory.getDishes();
  }

  private void assertDishEqual(Dish expected, Dish actual) {
    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getPrice(), actual.getPrice());
    assertEquals(expected.getWeight(), actual.getWeight());
    assertEquals(expected.getProtein(), actual.getProtein());
    assertEquals(expected.getFat(), actual.getFat());
    assertEquals(expected.getCarbohydrates(), actual.getCarbohydrates());
    assertEquals(expected.getImagePath(), actual.getImagePath());
    assertEquals(expected.getEmotions(), actual.getEmotions());
  }
}