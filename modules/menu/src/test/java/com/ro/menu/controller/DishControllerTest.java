package com.ro.menu.controller;

import com.ro.auth.config.UnitTestSecurityConfig;
import com.ro.core.ApiConfig;
import com.ro.core.CoreTestUtils;
import com.ro.menu.config.MenuSecurityConfig;
import com.ro.menu.dto.mappers.DishDtoMapper;
import com.ro.menu.dto.objects.DishDto;
import com.ro.menu.model.Dish;
import com.ro.menu.service.DishService;
import org.hamcrest.Matchers;
import org.hibernate.mapping.Collection;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.swing.*;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DishController.class)
@Import({
    ApiConfig.class,
    MenuSecurityConfig.class,
    UnitTestSecurityConfig.class
})
class DishControllerTest {

  @MockBean
  private DishService dishService;

  @MockBean
  private DishDtoMapper dishDtoMapper;

  @Autowired
  private MockMvc mockMvc;

  @Test
  void search() throws Exception {
    List<DishDto> givenDishes = mockDishServiceGetByNameAndToDto("test_name");

    ResultActions resultActions = mockMvc.perform(get("/menu/dishes/search?name=test_name"));

    assertGetResponse(resultActions, givenDishes);
  }

  private List<DishDto> mockDishServiceGetByNameAndToDto(String name) {
    List<Dish> dishes = CoreTestUtils.getRandomObjectsList(2, Dish.class);

    Mockito.when(dishService.getByNameContaining(name))
        .thenReturn(dishes);

    List<DishDto> dishDtos = CoreTestUtils.getRandomObjectsList(dishes.size(), DishDto.class);

    Mockito.when(dishDtoMapper.toDto(dishes))
        .thenReturn(dishDtos);

    return dishDtos;
  }

  @Test
  void search_whenNotFoundedInService() throws Exception {
    mockDishServiceGetByNameEmptyResultAndToDto("test_name");

    ResultActions resultActions = mockMvc.perform(get("/menu/dishes/search?name=test_name"));

    assertGetResponse(resultActions, Collections.emptyList());
  }

  @Test
  void search_whenNameReposneParamterIsEmpty() throws Exception {
    ResultActions resultActions = mockMvc.perform(get("/menu/dishes/search"));

    assertGetResponse(resultActions, Collections.emptyList());
  }

  private void mockDishServiceGetByNameEmptyResultAndToDto(String name) {
    Mockito.when(dishService.getByNameContaining(name))
        .thenReturn(Collections.emptyList());

    Mockito.when(dishDtoMapper.toDto(Collections.emptyList()))
        .thenReturn(Collections.emptyList());
  }

  private void assertGetResponse(ResultActions resultActions, List<DishDto> expectedList) throws Exception {
    resultActions
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.*", Matchers.hasSize(expectedList.size())));

    for (int i = 0; i < expectedList.size(); i++) {
      DishDto expected = expectedList.get(i);
      resultActions
          .andExpect(jsonPath("$.[" + i + "].id", Matchers.equalTo(expected.getId()), Long.class))
          .andExpect(jsonPath("$.[" + i + "].name", Matchers.equalTo(expected.getName())))
          .andExpect(jsonPath("$.[" + i + "].description", Matchers.equalTo(expected.getDescription())))
          .andExpect(jsonPath("$.[" + i + "].price", Matchers.equalTo(expected.getPrice().intValue())))
          .andExpect(jsonPath("$.[" + i + "].weight", Matchers.equalTo(expected.getWeight().intValue())))
          .andExpect(jsonPath("$.[" + i + "].protein", Matchers.equalTo(expected.getProtein()), Double.class))
          .andExpect(jsonPath("$.[" + i + "].fat", Matchers.equalTo(expected.getFat()), Double.class))
          .andExpect(jsonPath("$.[" + i + "].carbohydrates", Matchers.equalTo(expected.getCarbohydrates()), Double.class))
          .andExpect(jsonPath("$.[" + i + "].imageUrl", Matchers.equalTo(expected.getImageUrl())))
          .andExpect(jsonPath("$.[" + i + "].likes.likeCount", Matchers.equalTo(expected.getLikes().getLikeCount())))
          .andExpect(jsonPath("$.[" + i + "].likes.dislikeCount", Matchers.equalTo(expected.getLikes().getDislikeCount())));
    }
  }


}