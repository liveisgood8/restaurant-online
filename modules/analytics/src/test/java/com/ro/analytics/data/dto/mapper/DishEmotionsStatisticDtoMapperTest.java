package com.ro.analytics.data.dto.mapper;

import com.ro.analytics.data.dto.object.DishEmotionsStatisticDto;
import com.ro.analytics.data.dto.object.DishOrdersStatisticDto;
import com.ro.analytics.data.dto.object.MinimalDishDto;
import com.ro.analytics.data.model.DishEmotionsStatistic;
import com.ro.analytics.data.model.DishOrdersStatistic;
import com.ro.core.CoreTestUtils;
import com.ro.menu.model.Dish;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.AdditionalMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class DishEmotionsStatisticDtoMapperTest {
  @Mock
  private MinimalDishDtoMapper minimalDishDtoMapper;

  private DishEmotionsStatisticDtoMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new DishEmotionsStatisticDtoMapperImpl(minimalDishDtoMapper);
  }

  @Test
  void toDto() {
    List<DishEmotionsStatistic> givenStatistic = CoreTestUtils.getRandomObjectsList(1, DishEmotionsStatistic.class);
    MinimalDishDto givenMinimalDishDto = mockMinimalDishDtoMapper(givenStatistic);

    DishEmotionsStatisticDto actualDto = mapper.toDto(givenStatistic.get(0));

    assertDtoEqualWithStatistic(actualDto, givenMinimalDishDto, givenStatistic.get(0));
  }

  @Test
  void toDto_whenList() {
    List<DishEmotionsStatistic> givenStatistic = CoreTestUtils.getRandomObjectsList(2, DishEmotionsStatistic.class);
    MinimalDishDto givenMinimalDishDto = mockMinimalDishDtoMapper(givenStatistic);

    List<DishEmotionsStatisticDto> actualDtos = mapper.toDto(givenStatistic);

    assertEquals(2, actualDtos.size());
    for (int i = 0; i < givenStatistic.size(); i++) {
      assertDtoEqualWithStatistic(actualDtos.get(i), givenMinimalDishDto, givenStatistic.get(i));
    }
  }

  private MinimalDishDto mockMinimalDishDtoMapper(List<DishEmotionsStatistic> givenStatistic) {
    MinimalDishDto minimalDishDto = CoreTestUtils.getRandomObject(MinimalDishDto.class);

    Mockito.when(minimalDishDtoMapper.toDto(Mockito.any(Dish.class)))
        .thenReturn(minimalDishDto);

    return minimalDishDto;
  }

  private void assertDtoEqualWithStatistic(DishEmotionsStatisticDto actual,
                                           MinimalDishDto expectedMinimalDishDto,
                                           DishEmotionsStatistic expected) {
    assertEquals(expectedMinimalDishDto, actual.getMinimalDish());
    assertEquals(expected.getLikesCount(), actual.getLikesCount());
    assertEquals(expected.getDislikesCount(), actual.getDislikesCount());
  }

}