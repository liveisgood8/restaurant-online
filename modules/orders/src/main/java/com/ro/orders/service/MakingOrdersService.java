package com.ro.orders.service;

import com.ro.auth.model.User;
import com.ro.core.model.Address;
import com.ro.core.model.TelephoneNumber;
import com.ro.core.repository.AddressRepository;
import com.ro.core.repository.TelephoneNumberRepository;
import com.ro.orders.dto.mapper.OrderDtoMapper;
import com.ro.orders.dto.objects.OrderDto;
import com.ro.orders.events.OrderEvent;
import com.ro.orders.model.BonusesTransaction;
import com.ro.orders.model.Order;
import com.ro.orders.model.PaymentMethod;
import com.ro.orders.repository.OrdersRepository;
import com.sun.istack.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;

@Service
public class MakingOrdersService {
  private final OrdersRepository ordersRepository;
  private final TelephoneNumberRepository telephoneNumberRepository;
  private final BonusesTransactionService bonusesTransactionService;
  private final AddressRepository addressRepository;
  private final OrderDtoMapper orderDtoMapper;
  private final ApplicationEventMulticaster eventMulticaster;

  @Autowired
  public MakingOrdersService(OrdersRepository ordersRepository,
                             TelephoneNumberRepository telephoneNumberRepository,
                             BonusesTransactionService bonusesTransactionService,
                             AddressRepository addressRepository,
                             OrderDtoMapper orderDtoMapper,
                             ApplicationEventMulticaster eventMulticaster) {
    this.ordersRepository = ordersRepository;
    this.telephoneNumberRepository = telephoneNumberRepository;
    this.bonusesTransactionService = bonusesTransactionService;
    this.addressRepository = addressRepository;
    this.orderDtoMapper = orderDtoMapper;
    this.eventMulticaster = eventMulticaster;
  }

  @Transactional
  public void approveOrder(Long id) {
    ordersRepository.setIsApprovedById(true, id);
  }

  @Transactional
  public Order save(OrderDto orderDto, @Nullable User user) {
    Order order = orderDtoMapper.toEntity(orderDto);
    order.setUser(user);

    handleOrderTelephoneNumber(order);
    handleOrderAddress(order);

    order.setIsApproved(order.getPaymentMethod().getName().equals(PaymentMethod.BY_CARD_ONLINE));

    if (user != null && orderDto.getSpentBonuses() != 0) {
      BonusesTransaction outcomeTransaction = new BonusesTransaction();
      outcomeTransaction.setUser(user);
      outcomeTransaction.setAmount(-1 * orderDto.getSpentBonuses());
      outcomeTransaction.setOrder(order);
      order.getBonusesTransactions().add(outcomeTransaction);
    }

    int bonuses = 0;
    if (user != null) {
      bonuses = calculateBonuses(order);
      if (bonuses > 0) {
        BonusesTransaction incomeTransaction = new BonusesTransaction();
        incomeTransaction.setUser(user);
        incomeTransaction.setAmount(bonuses);
        incomeTransaction.setOrder(order);
        order.getBonusesTransactions().add(incomeTransaction);
      }
    }

    order = ordersRepository.save(order);

    OrderEvent orderEvent = new OrderEvent(order, this);
    eventMulticaster.multicastEvent(orderEvent);

    return ordersRepository.save(order);
  }

  private void handleOrderTelephoneNumber(Order order) {
    TelephoneNumber telephoneNumber = telephoneNumberRepository.findByCountryCodeAndNationalNumber(
        order.getTelephoneNumber().getCountryCode(),
        order.getTelephoneNumber().getNationalNumber())
        .orElse(telephoneNumberRepository.save(order.getTelephoneNumber()));

    order.setTelephoneNumber(telephoneNumber);
  }

  private void handleOrderAddress(Order order) {
    ExampleMatcher addressMatcher = ExampleMatcher.matchingAll()
        .withIgnorePaths("id")
        .withIgnoreCase();

    Address address = addressRepository.findOne(Example.of(order.getAddress(), addressMatcher))
        .orElse(addressRepository.save(order.getAddress()));

    order.setAddress(address);
  }

  private int calculateBonuses(Order order) {
    return (int) Math.round(order.getTotalPrice() * 0.05);
  }
}
