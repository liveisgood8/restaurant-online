package com.ro.orders.data.dto.mapper;

import com.ro.core.CoreTestUtils;
import com.ro.core.data.mapper.ReferenceDtoMapper;
import com.ro.core.data.mapper.TelephoneNumberDtoMapper;
import com.ro.core.data.model.Address;
import com.ro.core.data.model.TelephoneNumber;
import com.ro.core.utils.TelephoneNumberUtils;
import com.ro.menu.dto.objects.DishDto;
import com.ro.menu.model.Dish;
import com.ro.orders.data.dto.objects.AddressDto;
import com.ro.orders.data.dto.objects.OrderDto;
import com.ro.orders.data.dto.objects.OrderPartDto;
import com.ro.orders.data.model.Order;
import com.ro.orders.data.model.OrderPart;
import com.ro.orders.data.model.PaymentMethod;
import com.ro.orders.data.repository.OrdersRepository;
import com.ro.orders.data.repository.PaymentMethodRepository;
import com.ro.orders.exception.PaymentMethodNotExistException;
import com.ro.orders.utils.OrderDataTestUtil;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
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
  private ReferenceDtoMapper referenceDtoMapper;

  @Mock
  private TelephoneNumberDtoMapper telephoneNumberDtoMapper;

  @Mock
  private OrdersRepository ordersRepository;

  @Captor
  private ArgumentCaptor<OrderPart> orderPartArgumentCaptor;

  @Captor
  private ArgumentCaptor<OrderPartDto> orderPartDtoArgumentCaptor;

  private OrderDtoMapper mapper;

  @BeforeEach
  void init() {
    mapper = new OrderDtoMapperImpl(addressDtoMapper, orderPartDtoMapper, telephoneNumberDtoMapper, referenceDtoMapper);
    mapper.setOrdersRepository(ordersRepository);
    mapper.setPaymentMethodRepository(paymentMethodRepository);
  }

  @Test
  void toDto() {
    String givenPhone = "+79135687412";
    Order givenOrder = CoreTestUtils.getRandomObject(Order.class);

    Mockito.when(telephoneNumberDtoMapper.toString(givenOrder.getTelephoneNumber())).thenReturn(givenPhone);

    OrderDto actualOrderDto = mapper.toDto(givenOrder);

    Mockito.verify(addressDtoMapper, Mockito.times(1)).toDto(givenOrder.getAddress());
    Mockito.verify(orderPartDtoMapper, Mockito.times(givenOrder.getOrderParts().size()))
        .toDto(orderPartArgumentCaptor.capture());


    assertEquals(givenOrder.getId(), actualOrderDto.getId());
    assertEquals(givenOrder.getPaymentMethod().getName(), actualOrderDto.getPaymentMethod());
    assertEquals(givenOrder.getReceivedBonuses(), actualOrderDto.getReceivedBonuses());
    assertEquals(givenOrder.getSpentBonuses(), actualOrderDto.getSpentBonuses());
    assertEquals(givenPhone, actualOrderDto.getPhone());
    assertEquals(givenOrder.getIsApproved(), actualOrderDto.getIsApproved());
    assertEquals(givenOrder.getCreatedAt(), actualOrderDto.getCreatedAt());
    assertEquals(givenOrder.getOrderParts(), new HashSet<>(orderPartArgumentCaptor.getAllValues()));
  }

  @Test
  void toDtoWithoutParts() {
    Order order = CoreTestUtils.getRandomObject(Order.class);
    OrderDto dto = mapper.toDtoWithoutParts(order);

    Mockito.verify(orderPartDtoMapper, Mockito.never()).toDto(Mockito.any());

    assertNull(dto.getOrderParts());
  }

  @Test
  void toEntity_whenOrderExists() {
    PaymentMethod givenPaymentMethod = CoreTestUtils.getRandomObject(PaymentMethod.class);
    TelephoneNumber givenTelephoneNumber = CoreTestUtils.getRandomObject(TelephoneNumber.class);
    Order givenResolvedOrder = CoreTestUtils.getRandomObject(Order.class);
    OrderDto givenOrderDto = CoreTestUtils.getRandomObject(OrderDto.class);
    givenOrderDto.setId(givenResolvedOrder.getId());

    Mockito.when(referenceDtoMapper.resolve(givenOrderDto, Order.class)).thenReturn(givenResolvedOrder);
    Mockito.when(telephoneNumberDtoMapper.toEntity(givenOrderDto.getPhone())).thenReturn(givenTelephoneNumber);
    Mockito.when(paymentMethodRepository.findByName(givenOrderDto.getPaymentMethod()))
        .thenReturn(Optional.of(givenPaymentMethod));
    Mockito.when(orderPartDtoMapper.toEntity(Mockito.any(OrderPartDto.class)))
        .thenReturn(CoreTestUtils.getRandomObject(OrderPart.class));

    Order actualOrder = mapper.toEntity(givenOrderDto);

    Mockito.verify(addressDtoMapper, Mockito.times(1))
        .toEntity(givenOrderDto.getAddress());
    Mockito.verify(orderPartDtoMapper, Mockito.times(givenOrderDto.getOrderParts().size()))
        .toEntity(orderPartDtoArgumentCaptor.capture());

    assertEquals(givenOrderDto.getId(), actualOrder.getId());
    assertEquals(givenOrderDto.getIsApproved(), actualOrder.getIsApproved());
    assertEquals(givenPaymentMethod, actualOrder.getPaymentMethod());
    assertEquals(givenTelephoneNumber, actualOrder.getTelephoneNumber());
    assertEquals(0, actualOrder.getSpentBonuses());
    assertEquals(0, actualOrder.getReceivedBonuses());
    assertThat(actualOrder.getOrderParts().stream().map(OrderPart::getOrder).collect(Collectors.toList()),
        everyItem(equalTo(actualOrder)));
    assertThat(actualOrder.getOrderParts().stream().map(part -> part.getId().getOrderId()).collect(Collectors.toList()),
        everyItem(equalTo(actualOrder.getId())));

    assertEquals(givenOrderDto.getOrderParts(), new HashSet<>(orderPartDtoArgumentCaptor.getAllValues()));
  }

  @Test
  void toEntity_toEntity_whenOrderNotExists() {
    PaymentMethod givenPaymentMethod = CoreTestUtils.getRandomObject(PaymentMethod.class);
    TelephoneNumber givenTelephoneNumber = CoreTestUtils.getRandomObject(TelephoneNumber.class);
    OrderDto givenOrderDto = CoreTestUtils.getRandomObject(OrderDto.class);

    Mockito.when(referenceDtoMapper.resolve(givenOrderDto, Order.class)).thenReturn(new Order());
    Mockito.when(telephoneNumberDtoMapper.toEntity(givenOrderDto.getPhone())).thenReturn(givenTelephoneNumber);
    Mockito.when(paymentMethodRepository.findByName(givenOrderDto.getPaymentMethod()))
        .thenReturn(Optional.of(givenPaymentMethod));
    Mockito.when(orderPartDtoMapper.toEntity(Mockito.any(OrderPartDto.class)))
        .thenReturn(CoreTestUtils.getRandomObject(OrderPart.class));

    Order actualOrder = mapper.toEntity(givenOrderDto);

    Mockito.verify(paymentMethodRepository, Mockito.times(1))
        .findByName(givenOrderDto.getPaymentMethod());
    Mockito.verify(addressDtoMapper, Mockito.times(1))
        .toEntity(givenOrderDto.getAddress());
    Mockito.verify(orderPartDtoMapper, Mockito.times(givenOrderDto.getOrderParts().size()))
        .toEntity(orderPartDtoArgumentCaptor.capture());

    assertEquals(givenOrderDto.getId(), actualOrder.getId());
    assertEquals(givenOrderDto.getIsApproved(), actualOrder.getIsApproved());
    assertEquals(givenPaymentMethod, actualOrder.getPaymentMethod());
    assertEquals(givenTelephoneNumber, actualOrder.getTelephoneNumber());
    assertEquals(0, actualOrder.getSpentBonuses());
    assertEquals(0, actualOrder.getReceivedBonuses());
    assertThat(actualOrder.getOrderParts().stream().map(OrderPart::getOrder).collect(Collectors.toList()),
        everyItem(equalTo(actualOrder)));
    assertThat(actualOrder.getOrderParts().stream().map(part -> part.getId().getOrderId()).collect(Collectors.toList()),
        everyItem(equalTo(actualOrder.getId())));

    assertEquals(givenOrderDto.getOrderParts(), new HashSet<>(orderPartDtoArgumentCaptor.getAllValues()));
  }

  @Test
  void toEntity_whenPaymentMethodNotExists() {
     assertThrows(PaymentMethodNotExistException.class,
         () -> mapper.toEntity(CoreTestUtils.getRandomObject(OrderDto.class)));
  }
}