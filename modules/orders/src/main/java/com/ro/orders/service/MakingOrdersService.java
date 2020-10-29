package com.ro.orders.service;

import com.ro.auth.model.User;
import com.ro.core.model.Address;
import com.ro.core.repository.AddressRepository;
import com.ro.core.repository.TelephoneNumberRepository;
import com.ro.menu.model.Dish;
import com.ro.menu.repository.DishRepository;
import com.ro.orders.dto.mapper.OrderDtoMapper;
import com.ro.orders.dto.objects.OrderDto;
import com.ro.orders.events.OrderEvent;
import com.ro.orders.exception.PaymentMethodNotExistException;
import com.ro.orders.lib.OrderInfo;
import com.ro.orders.model.BonusesTransaction;
import com.ro.orders.model.Order;
import com.ro.orders.model.OrderPart;
import com.ro.orders.model.PaymentMethod;
import com.ro.orders.repository.OrderPartsRepository;
import com.ro.orders.repository.OrdersRepository;
import com.ro.orders.repository.PaymentMethodRepository;
import com.sun.istack.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class MakingOrdersService {
  private final OrdersRepository ordersRepository;
  private final OrderPartsRepository ordersInfoRepository;
  private final TelephoneNumberRepository telephoneNumberRepository;
  private final DishRepository dishRepository;
  private final PaymentMethodRepository paymentMethodRepository;
  private final BonusesTransactionService bonusesTransactionService;
  private final AddressRepository addressRepository;
  private final OrderDtoMapper orderDtoMapper;
  private final ApplicationEventMulticaster eventMulticaster;

  @Autowired
  public MakingOrdersService(OrdersRepository ordersRepository,
                             OrderPartsRepository ordersInfoRepository,
                             TelephoneNumberRepository telephoneNumberRepository,
                             DishRepository dishRepository,
                             PaymentMethodRepository paymentMethodRepository,
                             BonusesTransactionService bonusesTransactionService,
                             AddressRepository addressRepository,
                             OrderDtoMapper orderDtoMapper,
                             ApplicationEventMulticaster eventMulticaster) {
    this.ordersRepository = ordersRepository;
    this.ordersInfoRepository = ordersInfoRepository;
    this.telephoneNumberRepository = telephoneNumberRepository;
    this.dishRepository = dishRepository;
    this.paymentMethodRepository = paymentMethodRepository;
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
  public OrderInfo makeOrder(OrderDto orderDto, @Nullable User user) {
    Order order = orderDtoMapper.toEntity(orderDto);
    order.setUser(user);
    order.setIsApproved(order.getPaymentMethod().getName().equals(PaymentMethod.BY_CARD_ONLINE));

    prepareOrderForSave(order);
    Order finalOrder = ordersRepository.save(order);

    prepareOrderPartsForSave(finalOrder);
    List<OrderPart> savedOrderParts = ordersInfoRepository.saveAll(finalOrder.getOrderParts());
    finalOrder.setOrderParts(new HashSet<>(savedOrderParts));

    if (user != null && order.getSpentBonuses() != 0) {
      BonusesTransaction outcomeTransaction = bonusesTransactionService.addOutcome(order,
          user,
          orderDto.getSpentBonuses());
      finalOrder.getBonusesTransactions().add(outcomeTransaction);
    }

    int bonuses = 0;
    if (user != null) {
      bonuses = calculateBonuses(finalOrder);
      if (bonuses > 0) {
        BonusesTransaction incomeTransaction = bonusesTransactionService.addIncome(order,
                user,
                bonuses);
        finalOrder.getBonusesTransactions().add(incomeTransaction);
      }
    }

    OrderEvent orderEvent = new OrderEvent(finalOrder, this);
    eventMulticaster.multicastEvent(orderEvent);

    return new OrderInfo(finalOrder, bonuses);
  }

  private void prepareOrderForSave(Order order) {
    PaymentMethod paymentMethod = paymentMethodRepository.findByName(order.getPaymentMethod().getName())
        .orElseThrow(() -> new PaymentMethodNotExistException(order.getPaymentMethod().getName()));

    order.setPaymentMethod(paymentMethod);
    order.setAddress(handleOrderAddress(order.getAddress()));
    order.setTelephoneNumber(telephoneNumberRepository.save(order.getTelephoneNumber())); // TODO Check phone existence
  }

  private void prepareOrderPartsForSave(Order finalOrder) {
    finalOrder.getOrderParts().forEach(o -> {
      Dish dish = dishRepository.findById(o.getDish().getId())
          .orElseThrow(() -> new EntityNotFoundException("Dish with id: " + o.getDish().getId() + " not founded"));

      o.getId().setOrderId(finalOrder.getId());
      o.setOrder(finalOrder);
      o.setDish(dish);
    });
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
