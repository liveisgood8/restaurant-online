package com.ro.orders.dto.mapper;

import com.ro.orders.dto.objects.OrderPartDto;
import com.ro.orders.model.OrderPart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface OrderPartDtoMapper {
  @Mapping(target = "orderId", source = "id.orderId")
  @Mapping(target = "dishId", source = "id.dishId")
  OrderPartDto toDto(OrderPart orderPart);

  @Mapping(target = "id.orderId", source = "orderId")
  @Mapping(target = "id.dishId", source = "dishId")
  @Mapping(target = "dish.id", source = "dishId")
  OrderPart toEntity(OrderPartDto orderPartDto);
}
