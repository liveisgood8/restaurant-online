package com.ro.orders.dto.mapper;

import com.ro.orders.dto.objects.OrderInfoDto;
import com.ro.orders.model.OrderInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface OrderInfoDtoMapper {
  @Mapping(target = "orderId", source = "id.orderId")
  @Mapping(target = "dishId", source = "id.dishId")
  OrderInfoDto toDto(OrderInfo orderInfo);

  @Mapping(target = "id.orderId", source = "orderId")
  @Mapping(target = "id.dishId", source = "dishId")
  OrderInfo toEntity(OrderInfoDto orderInfoDto);
}
