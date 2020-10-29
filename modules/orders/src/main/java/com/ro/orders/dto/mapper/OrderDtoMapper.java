package com.ro.orders.dto.mapper;

import com.ro.core.model.TelephoneNumber;
import com.ro.core.utils.TelephoneNumberUtils;
import com.ro.menu.dto.mappers.DishDtoMapper;
import com.ro.orders.dto.objects.OrderDto;
import com.ro.orders.lib.OrderInfo;
import com.ro.orders.model.Order;
import com.ro.orders.model.OrderPart;
import jdk.jfr.Name;
import org.mapstruct.*;

import java.util.List;

@Mapper(uses = { AddressDtoMapper.class, OrderPartDtoMapper.class },
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class OrderDtoMapper {
  @Mapping(target = "paymentMethod", source = "paymentMethod.name")
  @Mapping(target = "phone", source = "telephoneNumber", qualifiedByName = "telephoneNumberToString")
  public abstract OrderDto toDto(Order order);
  public abstract List<OrderDto> toDto(List<Order> orders);

  @InheritConfiguration
  @Named("toDtoWithoutParts")
  @Mapping(target = "orderParts", ignore = true)
  public abstract OrderDto toDtoWithoutParts(Order order);

  @IterableMapping(qualifiedByName = "toDtoWithoutParts")
  public abstract List<OrderDto> toDtoWithoutParts(List<Order> order);

  @Mapping(target = "phone", source = "order.telephoneNumber", qualifiedByName = "telephoneNumberToString")
  @Mapping(target = "paymentMethod", source = "order.paymentMethod.name")
  @Mapping(target = ".", source = "order")
  public abstract OrderDto toDto(OrderInfo orderInfo);

  @Mapping(target = "paymentMethod.name", source = "paymentMethod")
  @Mapping(target = "telephoneNumber", source = "phone", qualifiedByName = "stringToTelephoneNumber")
  @Mapping(target = "createdAt", ignore = true)
  public abstract Order toEntity(OrderDto orderDto);

  @Named("stringToTelephoneNumber")
  protected TelephoneNumber stringToTelephoneNumber(String phone) {
    return TelephoneNumberUtils.fromString(phone);
  }

  @Named("telephoneNumberToString")
  protected String telephoneNumberToString(TelephoneNumber telephoneNumber) {
    return TelephoneNumberUtils.toString(telephoneNumber);
  }
}
