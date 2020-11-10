package com.ro.analytics.data.dto.mapper;

import com.ro.analytics.data.dto.object.DishOrderStatisticDto;
import com.ro.analytics.data.lib.DishOrderStatistic;
import com.ro.core.CoreTestUtils;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DishOrderStatisticDtoMapperTest {

  private final DishOrderStatisticDtoMapper mapper = Mappers.getMapper(DishOrderStatisticDtoMapper.class);

  @Test
  void toDto() {
    DishOrderStatistic givenStatistic = CoreTestUtils.getRandomObject(DishOrderStatistic.class);

    DishOrderStatisticDto actualDto = mapper.toDto(givenStatistic);
    assertDtoEqualWithStatistic(actualDto, givenStatistic);
  }

  @Test
  void toDto_whenList() {
    List<DishOrderStatistic> givenStatistic = List.of(
        CoreTestUtils.getRandomObject(DishOrderStatistic.class),
        CoreTestUtils.getRandomObject(DishOrderStatistic.class)
    );

    List<DishOrderStatisticDto> actualDtos = mapper.toDto(givenStatistic);

    assertEquals(2, actualDtos.size());
    for (int i = 0; i < givenStatistic.size(); i++) {
      assertDtoEqualWithStatistic(actualDtos.get(i), givenStatistic.get(i));
    }
  }

  private void assertDtoEqualWithStatistic(DishOrderStatisticDto actual, DishOrderStatistic expected) {
    assertEquals(expected.getDish().getId(), actual.getMinimalDish().getId());
    assertEquals(expected.getDish().getName(), actual.getMinimalDish().getName());
    assertEquals(expected.getNumberOfOrders(), actual.getNumberOfOrders());
  }

}