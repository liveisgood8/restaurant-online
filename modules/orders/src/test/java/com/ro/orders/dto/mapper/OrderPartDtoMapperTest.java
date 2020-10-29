package com.ro.orders.dto.mapper;

import com.ro.menu.dto.mappers.DishDtoMapper;
import com.ro.menu.dto.objects.DishDto;
import com.ro.menu.model.Dish;
import com.ro.menu.repository.DishRepository;
import com.ro.orders.dto.objects.OrderPartDto;
import com.ro.orders.model.Order;
import com.ro.orders.model.OrderPart;
import com.ro.orders.repository.OrdersRepository;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class OrderPartDtoMapperTest {
  @Mock
  private OrdersRepository ordersRepository;

  @Mock
  private DishRepository dishRepository;

  @Mock
  private DishDtoMapper dishDtoMapper;

  private OrderPartDtoMapper mapper;

  @BeforeEach
  void init() {
    mapper = new OrderPartDtoMapperImpl(dishDtoMapper);
    mapper.setOrdersRepository(ordersRepository);
    mapper.setDishRepository(dishRepository);
  }

  @Test
  void toEntity() {
    EasyRandom easyRandom = new EasyRandom();
    OrderPartDto orderDto = easyRandom.nextObject(OrderPartDto.class);

    Order order = easyRandom.nextObject(Order.class);
    order.setId(orderDto.getOrderId());

    Dish dish = easyRandom.nextObject(Dish.class);

    Mockito.doReturn(Optional.of(order)).when(ordersRepository).findById(orderDto.getOrderId());
    Mockito.doReturn(Optional.of(dish)).when(dishRepository).findById(orderDto.getDish().getId());

    OrderPart orderPart = mapper.toEntity(orderDto);

    assertEquals(orderPart.getId().getOrderId(), orderDto.getOrderId());
    assertEquals(orderPart.getId().getDishId(), orderDto.getDish().getId());
    assertEquals(orderPart.getDish(), dish);
    assertEquals(orderPart.getTotalPrice(), orderDto.getTotalPrice());
    assertEquals(orderPart.getCount(), orderDto.getCount());
  }

  @Test
  void toEntity_whenDishNotFounded() {
    EasyRandom easyRandom = new EasyRandom();
    OrderPartDto orderDto = easyRandom.nextObject(OrderPartDto.class);

    Order order = easyRandom.nextObject(Order.class);
    order.setId(orderDto.getOrderId());

    Mockito.doReturn(Optional.of(order)).when(ordersRepository).findById(orderDto.getOrderId());
    Mockito.doReturn(Optional.empty()).when(dishRepository).findById(orderDto.getOrderId());

    assertThrows(EntityNotFoundException.class, () -> mapper.toEntity(orderDto));
  }

  @Test
  void toEntity_whenOrderNotFounded() {
    EasyRandom easyRandom = new EasyRandom();
    OrderPartDto partDto = easyRandom.nextObject(OrderPartDto.class);

    Dish dish = easyRandom.nextObject(Dish.class);

    Mockito.doReturn(Optional.empty()).when(ordersRepository).findById(partDto.getOrderId());
    Mockito.doReturn(Optional.of(dish)).when(dishRepository).findById(partDto.getDish().getId());

    assertThrows(EntityNotFoundException.class, () -> mapper.toEntity(partDto));
  }

  @Test
  void toEntity_whenOrderIdIsNull() {
    EasyRandom easyRandom = new EasyRandom();
    OrderPartDto partDto = easyRandom.nextObject(OrderPartDto.class);
    partDto.setOrderId(null);

    Dish dish = easyRandom.nextObject(Dish.class);
    Mockito.doReturn(Optional.of(dish)).when(dishRepository).findById(partDto.getDish().getId());

    OrderPart entity = mapper.toEntity(partDto);
    assertNull(entity.getId().getOrderId());
    assertNull(entity.getOrder());
  }

  @Test
  void toDto() {
    EasyRandom easyRandom = new EasyRandom();
    OrderPart orderPart = easyRandom.nextObject(OrderPart.class);
    orderPart.getId().setOrderId(orderPart.getOrder().getId());

    DishDto dishDto = easyRandom.nextObject(DishDto.class);
    orderPart.getId().setDishId(dishDto.getId());
    orderPart.getDish().setId(dishDto.getId());

    Mockito.doReturn(dishDto).when(dishDtoMapper).toDto(orderPart.getDish());

    OrderPartDto orderPartDto = mapper.toDto(orderPart);

    assertEquals(orderPart.getId().getOrderId(), orderPartDto.getOrderId());
    assertEquals(orderPart.getId().getDishId(), orderPartDto.getDish().getId());
    assertEquals(orderPart.getTotalPrice(), orderPartDto.getTotalPrice());
    assertEquals(orderPart.getCount(), orderPartDto.getCount());
    assertEquals(dishDto, orderPartDto.getDish());
  }
}