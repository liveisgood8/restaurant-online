package com.ro.menu.dto.mappers;

import com.ro.core.utils.dto.AbstractMapper;
import com.ro.menu.dto.objects.DishDto;
import com.ro.menu.dto.objects.DishLikesDto;
import com.ro.menu.model.Dish;
import com.sun.istack.Nullable;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DishDtoMapper extends AbstractMapper<Dish, DishDto> {
  public DishDtoMapper() {
    super(Dish.class, DishDto.class);
  }

  @PostConstruct
  public void setupMapper() {
    mapper.createTypeMap(Dish.class, DishDto.class)
        .addMappings(m -> {
          m.skip(DishDto::setImageUrl);
          m.skip(DishDto::setLikes);
        }).setPostConverter(toDtoConverter());
  }

  @Override
  protected void mapSpecificEntityFields(Dish source, DishDto destination) {
    destination.setImageUrl(makeImageUrl(source.getId(), source.getImagePath()));
    destination.setLikes(DishLikesDto.fromEmotions(source.getEmotions()));
  }

  public static String makeImageUrl(Long dishId, @Nullable String imagePath) {
    String fileNamePart = imagePath != null ?
        FilenameUtils.getBaseName(imagePath).substring(0, 15) : "0";

    return String.format("/menu/dishes/%s/image?%s", dishId, fileNamePart);
  }
}
