package com.ro.orders.dto.mapper;

import com.ro.orders.dto.objects.OrderWithBonusesDto;
import com.ro.orders.service.OrderWithBonuses;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(imports = { OrderDtoMapper.class, OrderInfoDtoMapper.class })
public interface OrderWithBonusesDtoMapper {
  OrderWithBonusesDtoMapper INSTANCE = Mappers.getMapper(OrderWithBonusesDtoMapper.class);

  OrderWithBonusesDto toDto(OrderWithBonuses orderWithBonuses);
}
