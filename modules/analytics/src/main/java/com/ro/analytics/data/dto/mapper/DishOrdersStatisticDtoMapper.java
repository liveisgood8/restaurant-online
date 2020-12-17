package com.ro.analytics.data.dto.mapper;

import com.ro.analytics.data.dto.object.DishEmotionsStatisticDto;
import com.ro.analytics.data.dto.object.DishOrdersStatisticDto;
import com.ro.analytics.data.model.DishEmotionsStatistic;
import com.ro.analytics.data.model.DishOrdersStatistic;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    componentModel = "spring",
    uses = { MinimalDishDtoMapper.class }
)
public interface DishOrdersStatisticDtoMapper {
  @Mapping(source = "dish", target = "minimalDish")
  DishOrdersStatisticDto toDto(DishOrdersStatistic statistic);
  List<DishOrdersStatisticDto> toDto(List<DishOrdersStatistic> statistic);
}
