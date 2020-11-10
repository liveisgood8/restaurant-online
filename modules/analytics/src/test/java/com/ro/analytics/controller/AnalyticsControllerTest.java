package com.ro.analytics.controller;

import com.ro.analytics.data.dto.mapper.DishOrderStatisticDtoMapper;
import com.ro.analytics.data.dto.object.DishOrderStatisticDto;
import com.ro.analytics.data.lib.DishOrderStatistic;
import com.ro.analytics.service.OrderAnalyticsService;
import com.ro.core.CoreTestUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AnalyticsController.class)
@WithMockUser(authorities = "ADMIN")
class AnalyticsControllerTest {

  @MockBean
  private OrderAnalyticsService orderAnalyticsService;

  @MockBean
  private DishOrderStatisticDtoMapper dishOrderStatisticDtoMapper;

  @Autowired
  private MockMvc mockMvc;

  @Test
  @WithMockUser(authorities = "ADMIN")
  void getAnalyticsByOrders() throws Exception {
    List<DishOrderStatisticDto> givenDtos = mockAnalyticsServiceAndMapper(1);

    mockMvc.perform(get("/analytics/orders?topCount=1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.*", hasSize(1)))
        .andExpect(jsonPath("$.[0].minimalDish.id", is(givenDtos.get(0).getMinimalDish().getId())))
        .andExpect(jsonPath("$.[0].minimalDish.name", equalTo(givenDtos.get(0).getMinimalDish().getName())))
        .andExpect(jsonPath("$.[0].numberOfOrders", equalTo(givenDtos.get(0).getNumberOfOrders())));
  }

  @Test
  @WithMockUser
  void getAnalyticsByOrders_whenNonAdmin() throws Exception {
    // TODO Доделать авторизацию
    mockMvc.perform(get("/analytics/orders?topCount=1"))
        .andExpect(status().isForbidden());
  }

  private List<DishOrderStatisticDto> mockAnalyticsServiceAndMapper(int topCount) {
    List<DishOrderStatistic> dishOrderStatistics = List.of(CoreTestUtils.getRandomObject(DishOrderStatistic.class));
    Mockito.when(orderAnalyticsService.getStatisticByDishes(topCount))
        .thenReturn(dishOrderStatistics);

    List<DishOrderStatisticDto> dishOrderStatisticDtos = List.of(CoreTestUtils.getRandomObject(DishOrderStatisticDto.class));
    Mockito.when(dishOrderStatisticDtoMapper.toDto(dishOrderStatistics))
        .thenReturn(dishOrderStatisticDtos);

    return dishOrderStatisticDtos;
  }
}