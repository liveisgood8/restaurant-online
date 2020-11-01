package com.ro.orders.dto.mapper;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.ro.core.model.Address;
import com.ro.core.utils.TelephoneNumberUtils;
import com.ro.menu.dto.objects.DishDto;
import com.ro.orders.dto.objects.AddressDto;
import com.ro.orders.dto.objects.OrderDto;
import com.ro.orders.dto.objects.OrderPartDto;
import com.ro.orders.model.Order;
import com.ro.orders.model.OrderPart;
import com.ro.orders.model.PaymentMethod;
import com.ro.orders.repository.OrdersRepository;
import com.ro.orders.repository.PaymentMethodRepository;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class OrderDtoMapperTest {
  @Mock
  private OrderPartDtoMapper orderPartDtoMapper;

  @Mock
  private AddressDtoMapper addressDtoMapper;

  @Mock
  private PaymentMethodRepository paymentMethodRepository;

  @Mock
  private OrdersRepository ordersRepository;

  private OrderDtoMapper mapper;

  @BeforeEach
  void init() {
    mapper = new OrderDtoMapperImpl(addressDtoMapper, orderPartDtoMapper);
    mapper.setOrdersRepository(ordersRepository);
    mapper.setPaymentMethodRepository(paymentMethodRepository);
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
    EasyRandom easyRandom = new EasyRandom();

    Order order = easyRandom.nextObject(Order.class);
    order.setOrderParts(Set.of(easyRandom.nextObject(OrderPart.class), easyRandom.nextObject(OrderPart.class)));

    OrderDto dto = mapper.toDtoWithoutParts(order);

    assertNull(dto.getOrderParts());
  }

  @Test
  void toEntity_whenDtoHasId() {
    EasyRandom easyRandom = new EasyRandom();

    PaymentMethod paymentMethod = easyRandom.nextObject(PaymentMethod.class);
    paymentMethod.setName(PaymentMethod.BY_CARD_ONLINE);

    OrderPartDto partDto = easyRandom.nextObject(OrderPartDto.class);
    OrderDto dto = easyRandom.nextObject(OrderDto.class);
    dto.setPhone("+79235941222");
    dto.setPaymentMethod(paymentMethod.getName());
    dto.setOrderParts(Set.of(partDto));

    Order entity = easyRandom.nextObject(Order.class);
    OrderPart partEntity = easyRandom.nextObject(OrderPart.class);
    partEntity.getDish().setPrice(partDto.getDish().getPrice());
    partEntity.setCount(partDto.getCount());

    Mockito.when(paymentMethodRepository.findByName(dto.getPaymentMethod())).thenReturn(Optional.of(paymentMethod));
    Mockito.when(ordersRepository.findWithPartsById(dto.getId())).thenReturn(Optional.of(entity));
    Mockito.when(addressDtoMapper.toEntity(dto.getAddress())).thenReturn(easyRandom.nextObject(Address.class));
    Mockito.when(orderPartDtoMapper.toEntity(partDto)).thenReturn(partEntity);

    Order order = mapper.toEntity(dto);

    assertEquals(entity.getId(), order.getId());
    assertEquals(dto.getIsApproved(), order.getIsApproved());
    assertEquals(dto.getPaymentMethod(), order.getPaymentMethod().getName());
    assertEquals(dto.getPhone(), TelephoneNumberUtils.toString(order.getTelephoneNumber()));
    assertEquals(partEntity.getDish().getPrice() * partEntity.getCount() - order.getSpentBonuses(),
        order.getTotalPrice());
    assertEquals(entity.getSpentBonuses(), order.getSpentBonuses());
    assertEquals(entity.getReceivedBonuses(), order.getReceivedBonuses());
    assertNotNull(entity.getAddress());
  }

  @Test
  void toEntity_whenDtoHasNotId() {
    EasyRandom easyRandom = new EasyRandom();

    PaymentMethod paymentMethod = easyRandom.nextObject(PaymentMethod.class);
    paymentMethod.setName(PaymentMethod.BY_CARD_ONLINE);

    OrderPartDto partDto = easyRandom.nextObject(OrderPartDto.class);

    OrderDto dto = easyRandom.nextObject(OrderDto.class);
    dto.setId(null);
    dto.setPhone("+79235941222");
    dto.setPaymentMethod(paymentMethod.getName());
    dto.setSpentBonuses(312412);
    dto.setReceivedBonuses(3242342);
    dto.setOrderParts(Set.of(partDto));

    OrderPart partEntity = easyRandom.nextObject(OrderPart.class);
    partEntity.getDish().setPrice(partDto.getDish().getPrice());
    partEntity.setCount(partDto.getCount());

    Mockito.when(paymentMethodRepository.findByName(dto.getPaymentMethod())).thenReturn(Optional.of(paymentMethod));
    Mockito.when(addressDtoMapper.toEntity(dto.getAddress())).thenReturn(easyRandom.nextObject(Address.class));
    Mockito.when(orderPartDtoMapper.toEntity(partDto)).thenReturn(partEntity);

    Order order = mapper.toEntity(dto);

    assertNull(order.getId());
    assertEquals(dto.getIsApproved(), order.getIsApproved());
    assertEquals(dto.getPaymentMethod(), order.getPaymentMethod().getName());
    assertEquals(dto.getPhone(), TelephoneNumberUtils.toString(order.getTelephoneNumber()));
    assertEquals(partEntity.getDish().getPrice() * partEntity.getCount() - order.getSpentBonuses(),
        order.getTotalPrice());
    assertEquals(0, order.getSpentBonuses());
    assertEquals(0, order.getReceivedBonuses());
  }

  @Test
  void toEntity_whenEntityNotFoundedById() {
    EasyRandom easyRandom = new EasyRandom();

    assertThrows(EntityNotFoundException.class, () -> mapper.toEntity(easyRandom.nextObject(OrderDto.class)));
  }
}