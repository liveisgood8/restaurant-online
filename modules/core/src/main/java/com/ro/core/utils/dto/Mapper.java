package com.ro.core.utils.dto;

public interface Mapper<E, D> {
  E toEntity(D dto);
  D toDto(E entity);
  void mergeEntityWithDto(E entity, D dto);
}