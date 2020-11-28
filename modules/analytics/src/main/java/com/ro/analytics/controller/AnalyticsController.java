package com.ro.analytics.controller;

import com.ro.analytics.data.dto.mapper.DishEmotionsStatisticDtoMapper;
import com.ro.analytics.data.dto.mapper.DishOrdersStatisticDtoMapper;
import com.ro.analytics.data.dto.object.DishEmotionsStatisticDto;
import com.ro.analytics.data.dto.object.DishOrdersStatisticDto;
import com.ro.analytics.data.model.DishEmotionsStatistic;
import com.ro.analytics.data.model.DishOrdersStatistic;
import com.ro.analytics.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.basePath}/analytics")
public class AnalyticsController {
  private final AnalyticsService orderAnalyticsService;
  private final DishOrdersStatisticDtoMapper dishOrdersStatisticDtoMapper;
  private final DishEmotionsStatisticDtoMapper dishEmotionsStatisticDtoMapper;

  @Autowired
  public AnalyticsController(AnalyticsService orderAnalyticsService,
                             DishOrdersStatisticDtoMapper dishOrdersStatisticDtoMapper,
                             DishEmotionsStatisticDtoMapper dishEmotionsStatisticDtoMapper) {
    this.orderAnalyticsService = orderAnalyticsService;
    this.dishOrdersStatisticDtoMapper = dishOrdersStatisticDtoMapper;
    this.dishEmotionsStatisticDtoMapper = dishEmotionsStatisticDtoMapper;
  }

  @GetMapping("/dish-orders")
  public List<DishOrdersStatisticDto> getAnalyticsByDishOrders(@RequestParam(defaultValue = "5") Integer topCount) {
    List<DishOrdersStatistic> dishOrdersStatisticList = orderAnalyticsService.getDishOrdersStatistic(topCount);
    return dishOrdersStatisticDtoMapper.toDto(dishOrdersStatisticList);
  }

  @GetMapping("/dish-emotions")
  public List<DishEmotionsStatisticDto> getAnalyticsByDishEmotions(@RequestParam(defaultValue = "5") Integer topCount) {
    List<DishEmotionsStatistic> dishEmotionsStatisticList = orderAnalyticsService.getDishEmotionsStatistic(topCount);
    return dishEmotionsStatisticDtoMapper.toDto(dishEmotionsStatisticList);
  }
}
