package com.ro.analytics.data.dto.mapper;

import com.ro.analytics.data.dto.object.MinimalDishDto;
import com.ro.menu.model.Dish;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MinimalDishDtoMapper {
  @Mapping(source = "category.id", target = "categoryId")
  MinimalDishDto toDto(Dish dish);
}
