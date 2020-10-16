package com.ro.orders.service;

import com.ro.auth.model.User;
import com.ro.core.models.Address;
import com.ro.core.repository.AddressRepository;
import com.ro.core.repository.TelephoneNumberRepository;
import com.ro.menu.model.Dish;
import com.ro.menu.repository.DishRepository;
import com.ro.orders.dto.mapper.OrderDtoMapper;
import com.ro.orders.dto.objects.OrderDto;
import com.ro.orders.events.OrderEvent;
import com.ro.orders.lib.OrderInfo;
import com.ro.orders.model.BonusesTransaction;
import com.ro.orders.model.Order;
import com.ro.orders.model.OrderPart;
import com.ro.orders.repository.OrdersInfoRepository;
import com.ro.orders.repository.OrdersRepository;
import com.sun.istack.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class OrdersService {
  private final OrdersRepository ordersRepository;
  private final OrdersInfoRepository ordersInfoRepository;
  private final TelephoneNumberRepository telephoneNumberRepository;
  private final DishRepository dishRepository;
  private final BonusesTransactionService bonusesTransactionService;
  private final AddressRepository addressRepository;
  private final ApplicationEventMulticaster eventMulticaster;

  @Autowired
  public OrdersService(OrdersRepository ordersRepository,
                       OrdersInfoRepository ordersInfoRepository,
                       TelephoneNumberRepository telephoneNumberRepository,
                       DishRepository dishRepository,
                       BonusesTransactionService bonusesTransactionService,
                       AddressRepository addressRepository,
                       ApplicationEventMulticaster eventMulticaster) {
    this.ordersRepository = ordersRepository;
    this.ordersInfoRepository = ordersInfoRepository;
    this.telephoneNumberRepository = telephoneNumberRepository;
    this.dishRepository = dishRepository;
    this.bonusesTransactionService = bonusesTransactionService;
    this.addressRepository = addressRepository;
    this.eventMulticaster = eventMulticaster;
  }

  @Transactional
  public OrderInfo makeOrder(OrderDto orderDto, @Nullable User user) {
    Order order = OrderDtoMapper.INSTANCE.toEntity(orderDto);
    order.setUser(user);
    order.setIsApproved(orderDto.getPaymentMethod() == Order.PaymentMethod.BY_CARD_ONLINE);

    prepareOrderForSave(order);
    Order finalOrder = ordersRepository.save(order);

    order.getOrderParts().forEach(o -> {
      o.getId().setOrderId(finalOrder.getId());
      o.setOrder(finalOrder);
    });
    List<OrderPart> savedOrderParts = ordersInfoRepository.saveAll(finalOrder.getOrderParts());
    finalOrder.setOrderParts(new HashSet<>(savedOrderParts));

    if (user != null && orderDto.getSpentBonuses() != null && orderDto.getSpentBonuses() != 0) {
      BonusesTransaction outcomeTransaction = bonusesTransactionService.addOutcome(order,
              user,
              orderDto.getSpentBonuses());
      finalOrder.getTransactions().add(outcomeTransaction);
    }

    int bonuses = 0;
    if (user != null) {
      bonuses = calculateBonuses(finalOrder);
      if (bonuses > 0) {
        BonusesTransaction incomeTransaction = bonusesTransactionService.addIncome(order,
                user,
                bonuses);
        finalOrder.getTransactions().add(incomeTransaction);
      }
    }

    OrderEvent orderEvent = new OrderEvent(finalOrder, this);
    eventMulticaster.multicastEvent(orderEvent);

    return new OrderInfo(finalOrder, bonuses);
  }

  private void prepareOrderForSave(Order order) {
    order.setAddress(handleOrderAddress(order.getAddress()));
    order.setTelephoneNumber(telephoneNumberRepository.save(order.getTelephoneNumber())); // TODO Check phone existence
  }

  private Address handleOrderAddress(Address address) {
    ExampleMatcher addressMatcher = ExampleMatcher.matchingAll()
        .withIgnorePaths("id")
        .withIgnoreCase();

    Optional<Address> foundedAddress = addressRepository.findOne(Example.of(address, addressMatcher));
    return foundedAddress.orElseGet(() -> addressRepository.save(address));
  }

  private int calculateBonuses(Order order) {
    return (int) Math.round(order.getTotalPrice() * 0.05);
  }
}
