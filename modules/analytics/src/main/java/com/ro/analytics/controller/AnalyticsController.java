package com.ro.analytics.controller;

import com.ro.analytics.data.dto.mapper.DishOrderStatisticDtoMapper;
import com.ro.analytics.data.dto.object.DishOrderStatisticDto;
import com.ro.analytics.data.lib.DishOrderStatistic;
import com.ro.analytics.service.OrderAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {
  private final OrderAnalyticsService orderAnalyticsService;
  private final DishOrderStatisticDtoMapper dishOrderStatisticDtoMapper;

  @Autowired
  public AnalyticsController(OrderAnalyticsService orderAnalyticsService,
                             DishOrderStatisticDtoMapper dishOrderStatisticDtoMapper) {
    this.orderAnalyticsService = orderAnalyticsService;
    this.dishOrderStatisticDtoMapper = dishOrderStatisticDtoMapper;
  }

  @GetMapping("/orders")
  public List<DishOrderStatisticDto> getAnalyticsByOrders(@RequestParam(defaultValue = "5") Integer topCount) {
    List<DishOrderStatistic> dishOrderStatisticList = orderAnalyticsService.getStatisticByDishes(topCount);
    return dishOrderStatisticDtoMapper.toDto(dishOrderStatisticList);
  }
}
