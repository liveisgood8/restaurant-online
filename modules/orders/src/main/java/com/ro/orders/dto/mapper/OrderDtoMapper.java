package com.ro.orders.dto.mapper;

import com.ro.core.models.TelephoneNumber;
import com.ro.core.utils.TelephoneNumberUtils;
import com.ro.orders.dto.objects.OrderDto;
import com.ro.orders.lib.OrderInfo;
import com.ro.orders.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(uses = { OrderPartDtoMapper.class, AddressDtoMapper.class })
public interface OrderDtoMapper {
  OrderDtoMapper INSTANCE = Mappers.getMapper(OrderDtoMapper.class);

  @Mapping(target = "phone", source = "telephoneNumber", qualifiedByName = "telephoneNumberToString")
  OrderDto toDto(Order order);

  @Mapping(target = "phone", source = "order.telephoneNumber", qualifiedByName = "telephoneNumberToString")
  @Mapping(target = ".", source = "order")
  OrderDto toDto(OrderInfo orderInfo);

  @Mapping(target = "telephoneNumber", source = "phone", qualifiedByName = "stringToTelephoneNumber")
  @Mapping(target = "isApproved", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  Order toEntity(OrderDto orderDto);

  @Named("stringToTelephoneNumber")
  default TelephoneNumber stringToTelephoneNumber(String phone) {
    return TelephoneNumberUtils.fromString(phone);
  }

  @Named("telephoneNumberToString")
  default String telephoneNumberToString(TelephoneNumber telephoneNumber) {
    return TelephoneNumberUtils.toString(telephoneNumber);
  }
}
