package com.ro.analytics.data.dto.mapper;

import com.ro.analytics.data.dto.object.MinimalDishDto;
import com.ro.menu.model.Dish;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MinimalDishDtoMapper {
  MinimalDishDto toDto(Dish dish);
}
