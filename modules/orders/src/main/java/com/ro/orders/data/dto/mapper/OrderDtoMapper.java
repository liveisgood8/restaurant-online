package com.ro.orders.data.dto.mapper;

import com.ro.core.data.mapper.ReferenceDtoMapper;
import com.ro.core.data.mapper.TelephoneNumberDtoMapper;
import com.ro.core.data.model.TelephoneNumber;
import com.ro.core.utils.TelephoneNumberUtils;
import com.ro.orders.data.dto.objects.OrderDto;
import com.ro.orders.exception.PaymentMethodNotExistException;
import com.ro.orders.data.lib.OrderInfo;
import com.ro.orders.data.model.Order;
import com.ro.orders.data.model.PaymentMethod;
import com.ro.orders.data.repository.OrdersRepository;
import com.ro.orders.data.repository.PaymentMethodRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Mapper(uses = {AddressDtoMapper.class,
    OrderPartDtoMapper.class,
    TelephoneNumberDtoMapper.class,
    ReferenceDtoMapper.class},
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
  @Mapping(target = "phone", source = "telephoneNumber")
  public abstract OrderDto toDto(Order order);
  public abstract List<OrderDto> toDto(List<Order> orders);

  @InheritConfiguration
  @Named("toDtoWithoutParts")
  @Mapping(target = "orderParts", ignore = true)
  public abstract OrderDto toDtoWithoutParts(Order order);

  @IterableMapping(qualifiedByName = "toDtoWithoutParts")
  public abstract List<OrderDto> toDtoWithoutParts(List<Order> order);

  @Mapping(target = "phone", source = "order.telephoneNumber")
  @Mapping(target = "paymentMethod", source = "order.paymentMethod.name")
  @Mapping(target = ".", source = "order")
  public abstract OrderDto toDto(OrderInfo orderInfo);

  @Mapping(target = "paymentMethod", source = "paymentMethod", qualifiedByName = "toEntityPaymentMethod")
  @Mapping(target = "telephoneNumber", source = "phone")
  @Mapping(target = "createdAt", ignore = true)
  public abstract Order toEntity(OrderDto orderDto);

  @AfterMapping
  public void afterToEntity(OrderDto source, @MappingTarget Order target) {
    if (source.getOrderParts() != null &&  target.getOrderParts() != null && !source.getOrderParts().isEmpty()) {
      target.getOrderParts().forEach(part -> part.setOrder(target));
    }
  }

  @Named("toEntityPaymentMethod")
  protected PaymentMethod toEntityPaymentMethod(String paymentMethodName) {
    return paymentMethodRepository.findByName(paymentMethodName)
        .orElseThrow(() -> new PaymentMethodNotExistException(paymentMethodName));
  }
}
