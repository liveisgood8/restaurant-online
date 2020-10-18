package com.ro.orders.dto.mapper;

import com.ro.orders.dto.objects.MakeOrderDto;
import com.ro.orders.model.Order;
import com.ro.orders.model.OrderPart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = { OrderDtoMapper.class })
public interface MakeOrderDtoMapper {
  MakeOrderDtoMapper INSTANCE = Mappers.getMapper(MakeOrderDtoMapper.class);

  @Mapping(target = "telephoneNumber", source = "phone", qualifiedByName = "stringToTelephoneNumber")
  Order toOrderEntity(MakeOrderDto makeOrderDto);

  @Mapping(target = "id.orderId", source = "orderId")
  @Mapping(target = "id.dishId", source = "dishId")
  @Mapping(target = "dish.id", source = "dishId")
  OrderPart orderPartDtoToEntity(MakeOrderDto.OrderPartDto orderPartDto);
}
