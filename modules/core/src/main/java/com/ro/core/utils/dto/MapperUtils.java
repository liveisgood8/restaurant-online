package com.ro.core.utils.dto;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MapperUtils {
  private final ModelMapper modelMapper;

  @Autowired
  public MapperUtils(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  public <S, D> List<D> mapObjectsList(List<S> source, Class<D> clazz) {
    return source.stream()
        .map(e -> modelMapper.map(e, clazz))
        .collect(Collectors.toList());
  }

  public static <E, D> List<D> mapObjectsListToDto(List<E> entities, Mapper<E, D> mapper) {
    return entities.stream()
        .map(mapper::toDto)
        .collect(Collectors.toList());
  }
}
