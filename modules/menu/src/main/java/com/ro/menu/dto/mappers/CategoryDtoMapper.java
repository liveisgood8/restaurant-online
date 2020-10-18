package com.ro.menu.dto.mappers;

import com.ro.menu.dto.objects.CategoryDto;
import com.ro.menu.dto.objects.DishDto;
import com.ro.menu.model.Category;
import com.ro.menu.model.Dish;
import com.sun.istack.Nullable;
import org.apache.commons.io.FilenameUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CategoryDtoMapper {
  CategoryDtoMapper INSTANCE = Mappers.getMapper(CategoryDtoMapper.class);

  @Mapping(target = "imageUrl", expression = "java(ImageMapper.makeImageUrl(ImageMapper.ImageSource.CATEGORY, " +
      "category.getId(), category.getImagePath()))")
  CategoryDto toDto(Category category);
  List<CategoryDto> toDto(List<Category> categories);

  void updateFromDto(@MappingTarget Category category, CategoryDto categoryDto);
}
