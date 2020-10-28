package com.ro.orders.dto.mapper;

import com.ro.core.model.Address;
import com.ro.core.utils.TelephoneNumberUtils;
import com.ro.menu.dto.mappers.DishDtoMapper;
import com.ro.menu.dto.objects.DishDto;
import com.ro.menu.model.Dish;
import com.ro.orders.dto.objects.AddressDto;
import com.ro.orders.dto.objects.OrderDto;
import com.ro.orders.dto.objects.OrderPartDto;
import com.ro.orders.model.Order;
import com.ro.orders.model.OrderPart;
import liquibase.pro.packaged.A;
import liquibase.pro.packaged.E;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class OrderDtoMapperTest {
  @Mock
  private OrderPartDtoMapper orderPartDtoMapper;

  @Mock
  private AddressDtoMapper addressDtoMapper;

  private OrderDtoMapper mapper;

  @BeforeEach
  void init() {
    mapper = new OrderDtoMapperImpl(addressDtoMapper, orderPartDtoMapper);
  }

  @Test
  void toDto() {
    EasyRandom easyRandom = new EasyRandom();

    Order order = easyRandom.nextObject(Order.class);
    OrderPart firstOrderPart = easyRandom.nextObject(OrderPart.class);
    OrderPart secondOrderPart = easyRandom.nextObject(OrderPart.class);
    order.setOrderParts(Set.of(firstOrderPart, secondOrderPart));

    AddressDto addressDto = easyRandom.nextObject(AddressDto.class);

    DishDto secondDishDto = easyRandom.nextObject(DishDto.class);
    secondDishDto.setId(secondOrderPart.getDish().getId());
    secondOrderPart.setId(new OrderPart.OrderInfoId(secondDishDto.getId(), order.getId()));

    Mockito.doReturn(addressDto).when(addressDtoMapper).toDto(order.getAddress());
    Mockito.when(orderPartDtoMapper.toDto(Mockito.any(OrderPart.class)))
        .thenReturn(easyRandom.nextObject(OrderPartDto.class))
        .thenReturn(easyRandom.nextObject(OrderPartDto.class));

    OrderDto orderDto = mapper.toDto(order);

    assertEquals(order.getId(), orderDto.getId());
    assertEquals(addressDto, orderDto.getAddress());
    assertEquals(order.getPaymentMethod().getName(), orderDto.getPaymentMethod());
    assertEquals(order.getReceivedBonuses(), orderDto.getReceivedBonuses());
    assertEquals(order.getSpentBonuses(), orderDto.getSpentBonuses());
    assertEquals(TelephoneNumberUtils.toString(order.getTelephoneNumber()), orderDto.getPhone());
    assertEquals(order.getIsApproved(), orderDto.getIsApproved());
    assertEquals(order.getCreatedAt(), orderDto.getCreatedAt());
    assertEquals(orderDto.getOrderParts().size(), 2);
  }
  @Test
  void toDtoWithoutParts() {
  }

  @Test
  void toEntity() {
  }

  @Test
  void stringToTelephoneNumber() {
  }

  @Test
  void telephoneNumberToString() {
  }
}