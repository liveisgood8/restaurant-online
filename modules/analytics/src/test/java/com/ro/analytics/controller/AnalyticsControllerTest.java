package com.ro.analytics.controller;

import com.ro.analytics.config.AnalyticsSecurityConfig;
import com.ro.analytics.data.dto.mapper.DishEmotionsStatisticDtoMapper;
import com.ro.analytics.data.dto.mapper.DishOrdersStatisticDtoMapper;
import com.ro.analytics.data.dto.object.DishEmotionsStatisticDto;
import com.ro.analytics.data.dto.object.DishOrdersStatisticDto;
import com.ro.analytics.data.model.DishEmotionsStatistic;
import com.ro.analytics.data.model.DishOrdersStatistic;
import com.ro.analytics.service.AnalyticsService;
import com.ro.auth.config.UnitTestSecurityConfig;
import com.ro.core.ApiConfig;
import com.ro.core.CoreTestUtils;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AnalyticsController.class)
@Import({
    ApiConfig.class,
    AnalyticsSecurityConfig.class,
    UnitTestSecurityConfig.class
})
class AnalyticsControllerTest {
  @MockBean
  private AnalyticsService orderAnalyticsService;

  @MockBean
  private DishOrdersStatisticDtoMapper dishOrderStatisticDtoMapper;

  @MockBean
  private DishEmotionsStatisticDtoMapper dishEmotionsStatisticDtoMapper;

  @Autowired
  private MockMvc mockMvc;

  @Test
  @WithMockUser(authorities = "ADMIN")
  void getDishAnalyticsByOrders() throws Exception {
    List<DishOrdersStatisticDto> givenDtos = mockAnalyticsServiceDishOrdersAndMapper(1);

    mockMvc.perform(get("/analytics/dish-orders?topCount=1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.*", hasSize(1)))
        .andExpect(jsonPath("$.[0].minimalDish.id", is(givenDtos.get(0).getMinimalDish().getId()), Long.class))
        .andExpect(jsonPath("$.[0].minimalDish.name", equalTo(givenDtos.get(0).getMinimalDish().getName())))
        .andExpect(jsonPath("$.[0].minimalDish.categoryId", equalTo(givenDtos.get(0).getMinimalDish().getCategoryId()), Long.class))
        .andExpect(jsonPath("$.[0].ordersCount", equalTo(givenDtos.get(0).getOrdersCount())));
  }

  @Test
  @WithMockUser
  void getDishAnalyticsByOrders_whenNonAdmin() throws Exception {
    mockMvc.perform(get("/analytics/dish-orders?topCount=1"))
        .andExpect(status().isForbidden());
  }

  private List<DishOrdersStatisticDto> mockAnalyticsServiceDishOrdersAndMapper(int topCount) {
    List<DishOrdersStatistic> dishOrderStatistics = CoreTestUtils.getRandomObjectsList(topCount, DishOrdersStatistic.class);
    Mockito.when(orderAnalyticsService.getDishOrdersStatistic(topCount))
        .thenReturn(dishOrderStatistics);

    List<DishOrdersStatisticDto> dishOrderStatisticDtos = CoreTestUtils.getRandomObjectsList(topCount, DishOrdersStatisticDto.class);
    Mockito.when(dishOrderStatisticDtoMapper.toDto(dishOrderStatistics))
        .thenReturn(dishOrderStatisticDtos);

    return dishOrderStatisticDtos;
  }

  @Test
  @WithMockUser(authorities = "ADMIN")
  void getDishAnalyticsByEmotions() throws Exception {
    List<DishEmotionsStatisticDto> givenDtos = mockAnalyticsServiceDishEmotionsAndMapper(2);

    mockMvc.perform(get("/analytics/dish-emotions?topCount=2"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.*", hasSize(2)))
        .andExpect(jsonPath("$.[*].minimalDish.id", containsInAnyOrder(is(givenDtos.get(0).getMinimalDish().getId().intValue()),
            is(givenDtos.get(1).getMinimalDish().getId().intValue()))))
        .andExpect(jsonPath("$.[*].minimalDish.name", containsInAnyOrder(is(givenDtos.get(0).getMinimalDish().getName()),
            is(givenDtos.get(1).getMinimalDish().getName()))))
        .andExpect(jsonPath("$.[*].minimalDish.categoryId", containsInAnyOrder(
            equalTo(givenDtos.get(0).getMinimalDish().getCategoryId().intValue()),
            equalTo(givenDtos.get(1).getMinimalDish().getCategoryId().intValue()))))
        .andExpect(jsonPath("$.[*].likesCount", containsInAnyOrder(is(givenDtos.get(0).getLikesCount()),
            is(givenDtos.get(1).getLikesCount()))))
        .andExpect(jsonPath("$.[*].dislikesCount", containsInAnyOrder(is(givenDtos.get(0).getDislikesCount()),
            is(givenDtos.get(1).getDislikesCount()))));
  }

  @Test
  @WithMockUser
  void getDishAnalyticsByEmotions_whenNonAdmin() throws Exception {
    mockMvc.perform(get("/analytics/dish-emotions?topCount=1"))
        .andExpect(status().isForbidden());
  }

  private List<DishEmotionsStatisticDto> mockAnalyticsServiceDishEmotionsAndMapper(int topCount) {
    List<DishEmotionsStatistic> dishEmotionsStatistics = CoreTestUtils.getRandomObjectsList(topCount, DishEmotionsStatistic.class);
    Mockito.when(orderAnalyticsService.getDishEmotionsStatistic(topCount))
        .thenReturn(dishEmotionsStatistics);

    List<DishEmotionsStatisticDto> dishEmotionsStatisticDtos = CoreTestUtils.getRandomObjectsList(topCount, DishEmotionsStatisticDto.class);
    Mockito.when(dishEmotionsStatisticDtoMapper.toDto(dishEmotionsStatistics))
        .thenReturn(dishEmotionsStatisticDtos);

    return dishEmotionsStatisticDtos;
  }


}