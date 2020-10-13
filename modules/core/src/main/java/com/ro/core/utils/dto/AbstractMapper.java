package com.ro.core.utils.dto;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

public class AbstractMapper<E, D> implements Mapper<E, D>  {
  protected ModelMapper mapper;
  private final Class<E> entityClass;
  private final Class<D> dtoClass;

  protected AbstractMapper(Class<E> entityClass, Class<D> dtoClass) {
    this.entityClass = entityClass;
    this.dtoClass = dtoClass;
  }

  @Autowired
  public void setMapper(ModelMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public E toEntity(D dto) {
    return Objects.isNull(dto)
        ? null
        : mapper.map(dto, entityClass);
  }

  @Override
  public D toDto(E entity) {
    return Objects.isNull(entity)
        ? null
        : mapper.map(entity, dtoClass);
  }

  @Override
  public void mergeEntityWithDto(E entity, D dto) {
    if (!Objects.isNull(entity) && !Objects.isNull(dto)) {
        mapper.map(dto, entity);
    }
  }

  protected Converter<E, D> toDtoConverter() {
    return context -> {
      E source = context.getSource();
      D destination = context.getDestination();
      mapSpecificEntityFields(source, destination);
      return context.getDestination();
    };
  }

  protected Converter<D, E> toEntityConverter() {
    return context -> {
      D source = context.getSource();
      E destination = context.getDestination();
      mapSpecificDtoFields(source, destination);
      return context.getDestination();
    };
  }

  protected void mapSpecificEntityFields(E source, D destination) {
  }

  protected void mapSpecificDtoFields(D source, E destination) {
  }
}
