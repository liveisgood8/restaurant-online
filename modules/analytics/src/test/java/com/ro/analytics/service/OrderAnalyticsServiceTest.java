package com.ro.analytics.service;

import com.ro.analytics.data.model.DishEmotionsStatistic;
import com.ro.analytics.data.model.DishOrdersStatistic;
import com.ro.analytics.data.repository.DishEmotionsStatisticRepository;
import com.ro.analytics.data.repository.DishOrdersStatisticRepository;
import com.ro.core.CoreTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class OrderAnalyticsServiceTest {

  @Mock
  private DishOrdersStatisticRepository dishOrdersStatisticRepository;

  @Mock
  private DishEmotionsStatisticRepository dishEmotionsStatisticRepository;

  private AnalyticsService service;

  @BeforeEach
  void setUp() {
    service = new AnalyticsService(dishOrdersStatisticRepository, dishEmotionsStatisticRepository);
  }

  @Test
  void getDishOrdersStatistic() {
    List<DishOrdersStatistic> givenStatistic = mockDishOrderStatisticRepository(2);

    List<DishOrdersStatistic> actualStatistic = service.getDishOrdersStatistic(2);

    assertEquals(givenStatistic, actualStatistic);
  }

  private List<DishOrdersStatistic> mockDishOrderStatisticRepository(int topCount) {
    List<DishOrdersStatistic> ordersStatisticsList = CoreTestUtils.getRandomObjectsList(topCount, DishOrdersStatistic.class);

    Pageable pageable = PageRequest.of(0, topCount);
    Mockito.when(dishOrdersStatisticRepository.findAll(pageable))
        .thenReturn(new PageImpl<>(ordersStatisticsList, pageable, 12));

    return ordersStatisticsList;
  }

  @Test
  void getDishEmotionsStatistic() {
    List<DishEmotionsStatistic> givenStatistic = mockDishEmotionsStatisticRepository(2);

    List<DishEmotionsStatistic> actualStatistic = service.getDishEmotionsStatistic(2);

    assertEquals(givenStatistic, actualStatistic);
  }

  private List<DishEmotionsStatistic> mockDishEmotionsStatisticRepository(int topCount) {
    List<DishEmotionsStatistic> emotionsStatisticsList =
        CoreTestUtils.getRandomObjectsList(topCount, DishEmotionsStatistic.class);

    Pageable pageable = PageRequest.of(0, topCount);
    Mockito.when(dishEmotionsStatisticRepository.findAll(pageable))
        .thenReturn(new PageImpl<>(emotionsStatisticsList, pageable, 12));

    return emotionsStatisticsList;
  }

}