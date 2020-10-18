package com.ro.menu.dto.mappers;

import com.ro.menu.dto.objects.DishDto;
import com.ro.menu.model.Dish;
import com.ro.menu.model.DishEmotion;
import com.sun.istack.Nullable;
import org.apache.commons.io.FilenameUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper
public interface DishDtoMapper {
  DishDtoMapper INSTANCE = Mappers.getMapper(DishDtoMapper.class);

  @Mapping(target = "imageUrl", expression = "java(ImageMapper.makeImageUrl(ImageMapper.ImageSource.DISH, " +
      "dish.getId(), dish.getImagePath()))")
  @Mapping(target = "likes", source = "emotions", qualifiedByName = "emotionsToDishDtoLikes")
  DishDto toDto(Dish dish);
  List<DishDto> toDto(List<Dish> dish);

  @Named("emotionsToDishDtoLikes")
  default DishDto.LikesDto emotionsToDishDtoLikes(Set<DishEmotion> emotions) {
    return new DishDto.LikesDto(emotions);
  }

  void updateFromDto(@MappingTarget Dish dish, DishDto dishDto);
}
