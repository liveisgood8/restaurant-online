package com.ro.analytics.service;

import com.ro.analytics.data.model.DishEmotionsStatistic;
import com.ro.analytics.data.model.DishOrdersStatistic;
import com.ro.analytics.data.repository.DishEmotionsStatisticRepository;
import com.ro.analytics.data.repository.DishOrdersStatisticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalyticsService {
  private final DishOrdersStatisticRepository dishOrdersStatisticRepository;
  private final DishEmotionsStatisticRepository dishEmotionsStatisticRepository;

  @Autowired
  public AnalyticsService(DishOrdersStatisticRepository dishOrdersStatisticRepository,
                          DishEmotionsStatisticRepository dishEmotionsStatisticRepository) {
    this.dishOrdersStatisticRepository = dishOrdersStatisticRepository;
    this.dishEmotionsStatisticRepository = dishEmotionsStatisticRepository;
  }


  public List<DishOrdersStatistic> getDishOrdersStatistic(int topCount) {
    return dishOrdersStatisticRepository.findAll(PageRequest.of(0, topCount))
        .toList();
  }

  public List<DishEmotionsStatistic> getDishEmotionsStatistic(int topCount) {
    return dishEmotionsStatisticRepository.findAll(PageRequest.of(0, topCount))
        .toList();
  }
}
