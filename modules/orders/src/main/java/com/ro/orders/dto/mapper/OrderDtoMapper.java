package com.ro.orders.dto.mapper;

import com.ro.core.model.TelephoneNumber;
import com.ro.core.utils.TelephoneNumberUtils;
import com.ro.menu.dto.mappers.DishDtoMapper;
import com.ro.orders.dto.objects.OrderDto;
import com.ro.orders.exception.PaymentMethodNotExistException;
import com.ro.orders.lib.OrderInfo;
import com.ro.orders.model.BonusesTransaction;
import com.ro.orders.model.Order;
import com.ro.orders.model.OrderPart;
import com.ro.orders.model.PaymentMethod;
import com.ro.orders.repository.OrdersRepository;
import com.ro.orders.repository.PaymentMethodRepository;
import jdk.jfr.Name;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Mapper(uses = { AddressDtoMapper.class, OrderPartDtoMapper.class },
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class OrderDtoMapper {
  private OrdersRepository ordersRepository;
  private PaymentMethodRepository paymentMethodRepository;

  @Autowired
  public void setOrdersRepository(OrdersRepository ordersRepository) {
    this.ordersRepository = ordersRepository;
  }

  @Autowired
  public void setPaymentMethodRepository(PaymentMethodRepository paymentMethodRepository) {
    this.paymentMethodRepository = paymentMethodRepository;
  }

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

  public Order toEntity(OrderDto orderDto) {
    Order order;
    if (orderDto.getId() == null) {
      order = new Order();
    } else {
      order = ordersRepository.findWithPartsById(orderDto.getId())
          .orElseThrow(() -> new EntityNotFoundException("Order with id: " + orderDto.getId() + " is not founded"));
    }

    mergeWithDto(orderDto, order);
    order.getOrderParts().forEach(p -> p.setOrder(order));

    return order;
  }

  @Mapping(target = "paymentMethod", source = "paymentMethod", qualifiedByName = "toEntityPaymentMethod")
  @Mapping(target = "telephoneNumber", source = "phone", qualifiedByName = "stringToTelephoneNumber")
  @Mapping(target = "createdAt", ignore = true)
  protected abstract void mergeWithDto(OrderDto dto, @MappingTarget Order order);

  @Named("toEntityPaymentMethod")
  protected PaymentMethod toEntityPaymentMethod(String paymentMethodName) {
    return paymentMethodRepository.findByName(paymentMethodName)
        .orElseThrow(() -> new PaymentMethodNotExistException(paymentMethodName));
  }

  @Named("stringToTelephoneNumber")
  protected TelephoneNumber stringToTelephoneNumber(String phone) {
    return TelephoneNumberUtils.fromString(phone);
  }

  @Named("telephoneNumberToString")
  protected String telephoneNumberToString(TelephoneNumber telephoneNumber) {
    return TelephoneNumberUtils.toString(telephoneNumber);
  }
}
