package com.ro.menu.dto.mappers;

import com.ro.menu.dto.objects.DishDto;
import com.ro.menu.model.Dish;
import com.sun.istack.Nullable;
import org.apache.commons.io.FilenameUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DishDtoMapper {
  DishDtoMapper INSTANCE = Mappers.getMapper(DishDtoMapper.class);

  @Mapping(target = "imageUrl", expression = "java(DishDtoMapper.makeImageUrl(dish.getId(), dish.getImagePath()))")
  @Mapping(target = "likes", expression = "java(new DishDto.LikesDto(dish.getEmotions()))")
  DishDto toDto(Dish dish);
  List<DishDto> toDto(List<Dish> dish);

  void updateFromDto(@MappingTarget Dish dish, DishDto dishDto);

  static String makeImageUrl(Long dishId, @Nullable String imagePath) {
    String fileNamePart = imagePath != null ?
        FilenameUtils.getBaseName(imagePath).substring(0, 15) : "0";

    return String.format("/menu/dishes/%s/image?%s", dishId, fileNamePart);
  }
}
