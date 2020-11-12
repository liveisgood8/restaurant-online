package com.ro.analytics.data.dto.mapper;

import com.ro.analytics.data.dto.object.DishEmotionsStatisticDto;
import com.ro.analytics.data.model.DishEmotionsStatistic;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    componentModel = "spring",
    uses = { MinimalDishDtoMapper.class }
)
public interface DishEmotionsStatisticDtoMapper {
  @Mapping(source = "dish", target = "minimalDish")
  DishEmotionsStatisticDto toDto(DishEmotionsStatistic statistic);
  List<DishEmotionsStatisticDto> toDto(List<DishEmotionsStatistic> statisticList);
}