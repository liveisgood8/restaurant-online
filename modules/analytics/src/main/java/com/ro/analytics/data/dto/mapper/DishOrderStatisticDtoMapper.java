package com.ro.analytics.data.dto.mapper;

import com.ro.analytics.data.dto.object.DishOrderStatisticDto;
import com.ro.analytics.data.lib.DishOrderStatistic;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR, componentModel = "spring")
public interface DishOrderStatisticDtoMapper {
  @Mapping(source = "dish.id", target = "minimalDish.id")
  @Mapping(source = "dish.name", target = "minimalDish.name")
  DishOrderStatisticDto toDto(DishOrderStatistic statistic);
  List<DishOrderStatisticDto> toDto(List<DishOrderStatistic> statistic);
}
