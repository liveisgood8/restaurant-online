package com.ro.orders.dto.mapper;

import com.ro.orders.dto.objects.OrderDto;
import com.ro.orders.model.Order;
import com.ro.orders.service.OrderWithBonuses;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(imports = { OrderInfoDtoMapper.class })
public interface OrderDtoMapper {
  OrderDtoMapper INSTANCE = Mappers.getMapper(OrderDtoMapper.class);

  OrderDto toDto(Order order);
}
