package com.ro.orders.service;

import com.ro.auth.data.model.User;
import com.ro.orders.data.dto.mapper.OrderDtoMapper;
import com.ro.orders.data.dto.objects.OrderDto;
import com.ro.orders.events.OrderEvent;
import com.ro.orders.data.model.BonusesTransaction;
import com.ro.orders.data.model.Order;
import com.ro.orders.data.model.PaymentMethod;
import com.ro.orders.data.repository.OrdersRepository;
import com.sun.istack.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MakingOrdersService {
  private final OrdersRepository ordersRepository;
  private final OrderDtoMapper orderDtoMapper;
  private final OrderContactsService orderContactsService;
  private final BonusesTransactionService bonusesTransactionService;
  private final ApplicationEventMulticaster eventMulticaster;

  @Autowired
  public MakingOrdersService(OrdersRepository ordersRepository,
                             OrderDtoMapper orderDtoMapper,
                             OrderContactsService orderContactsService,
                             BonusesTransactionService bonusesTransactionService,
                             ApplicationEventMulticaster eventMulticaster) {
    this.ordersRepository = ordersRepository;
    this.orderDtoMapper = orderDtoMapper;
    this.orderContactsService = orderContactsService;
    this.bonusesTransactionService = bonusesTransactionService;
    this.eventMulticaster = eventMulticaster;
  }

  @Transactional
  public Order save(OrderDto orderDto, @Nullable User user) {
    Order order = orderDtoMapper.toEntity(orderDto);
    order.setUser(user);

    orderContactsService.saveOrderContacts(order);

    order.setIsApproved(order.getPaymentMethod().getName().equals(PaymentMethod.BY_CARD_ONLINE));

    createBonusesTransactions(order, orderDto.getSpentBonuses());

    order = ordersRepository.save(order);

    OrderEvent orderEvent = new OrderEvent(order, this);
    eventMulticaster.multicastEvent(orderEvent);

    return ordersRepository.save(order);
  }

  private void createBonusesTransactions(Order order, Integer spentBonuses) {
    User user = order.getUser();
    if (user == null) {
      return;
    }

    if (spentBonuses != null && spentBonuses > 0) {
      BonusesTransaction outcomeTransaction = bonusesTransactionService.addOutcome(order, user, spentBonuses);
      order.getBonusesTransactions().add(outcomeTransaction);
    }

    int receivedBonuses = calculateBonuses(order);
    if (receivedBonuses > 0) {
      BonusesTransaction incomeTransaction = bonusesTransactionService.addIncome(order, user, receivedBonuses);
      order.getBonusesTransactions().add(incomeTransaction);
    }
  }

  private int calculateBonuses(Order order) {
    return (int) Math.round(order.getTotalPrice() * 0.05);
  }
}
