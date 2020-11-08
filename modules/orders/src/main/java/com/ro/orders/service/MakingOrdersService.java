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
  private final ApplicationEventMulticaster eventMulticaster;

  @Autowired
  public MakingOrdersService(OrdersRepository ordersRepository,
                             OrderDtoMapper orderDtoMapper,
                             OrderContactsService orderContactsService,
                             ApplicationEventMulticaster eventMulticaster) {
    this.ordersRepository = ordersRepository;

    this.orderDtoMapper = orderDtoMapper;
    this.orderContactsService = orderContactsService;
    this.eventMulticaster = eventMulticaster;
  }

  public void approveOrder(Long id) {
    ordersRepository.setIsApprovedById(true, id);
  }

  @Transactional
  public Order save(OrderDto orderDto, @Nullable User user) {
    Order order = orderDtoMapper.toEntity(orderDto);
    order.setUser(user);

    orderContactsService.saveOrderContacts(order);

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

  private int calculateBonuses(Order order) {
    return (int) Math.round(order.getTotalPrice() * 0.05);
  }
}
