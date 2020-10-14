package com.ro.orders.service;

import com.ro.auth.model.User;
import com.ro.auth.service.UserService;
import com.ro.core.models.Address;
import com.ro.core.repository.AddressRepository;
import com.ro.orders.controller.payloads.MakeOrderRequest;
import com.ro.orders.events.OrderEvent;
import com.ro.orders.model.Order;
import com.ro.orders.model.OrderInfo;
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
import java.util.stream.Collectors;

@Service
public class OrdersService {
  private final OrdersRepository ordersRepository;
  private final OrdersInfoRepository ordersInfoRepository;
  private final UserService userService;
  private final AddressRepository addressRepository;
  private final ApplicationEventMulticaster eventMulticaster;

  @Autowired
  public OrdersService(OrdersRepository ordersRepository,
                       OrdersInfoRepository ordersInfoRepository,
                       UserService userService,
                       AddressRepository addressRepository,
                       ApplicationEventMulticaster eventMulticaster) {
    this.ordersRepository = ordersRepository;
    this.ordersInfoRepository = ordersInfoRepository;
    this.userService = userService;
    this.addressRepository = addressRepository;
    this.eventMulticaster = eventMulticaster;
  }

  @Transactional
  public OrderWithBonuses makeOrder(MakeOrderRequest makeOrderRequest, @Nullable User user) {
    Order order = new Order();
    order.setUser(user);

    Set<OrderInfo> orderInfos = makeOrderRequest.getEntries().stream()
        .map(entry -> {
          OrderInfo orderInfo = new OrderInfo();
          orderInfo.getId().setDishId(entry.getDish().getId());
          orderInfo.setDish(entry.getDish());
          orderInfo.setCount(entry.getCount());
          orderInfo.setOrder(order);
          return orderInfo;
        })
        .collect(Collectors.toSet());

    Address address = handleOrderAddress(makeOrderRequest);

    order.setAddress(address);
    order.setPaymentMethod(makeOrderRequest.getPaymentMethod());
    order.setIsApproved(makeOrderRequest.getPaymentMethod() == Order.PaymentMethod.BY_CARD_ONLINE);
    order.setOrderInfos(orderInfos);

    Order savedOrder = ordersRepository.save(order);

    order.getOrderInfos().forEach(o -> o.getId().setOrderId(savedOrder.getId()));
    List<OrderInfo> savedOrderInfos = ordersInfoRepository.saveAll(order.getOrderInfos());
    savedOrder.setOrderInfos(new HashSet<>(savedOrderInfos));

    Integer bonuses = user == null ? 0 : creditBonusesToUser(savedOrder);

    OrderEvent orderEvent = new OrderEvent(savedOrder, this);
    eventMulticaster.multicastEvent(orderEvent);

    return new OrderWithBonuses(order, bonuses);
  }

  private Address handleOrderAddress(MakeOrderRequest makeOrderRequest) {
    Address address = new Address();
    address.setStreet(makeOrderRequest.getStreet());
    address.setHomeNumber(makeOrderRequest.getHomeNumber());
    address.setEntranceNumber(makeOrderRequest.getEntranceNumber());
    address.setFloorNumber(makeOrderRequest.getFloorNumber());
    address.setApartmentNumber(makeOrderRequest.getApartmentNumber());

    ExampleMatcher addressMatcher = ExampleMatcher.matchingAll()
        .withIgnorePaths("id")
        .withIgnoreCase();

    Optional<Address> foundedAddress = addressRepository.findOne(Example.of(address, addressMatcher));
    return foundedAddress.orElseGet(() -> addressRepository.save(address));
  }

  private Integer creditBonusesToUser(Order order) {
    Integer orderBonuses = calculateBonuses(order);
    userService.addBonuses(order.getUser().getId(), orderBonuses);
    return orderBonuses;
  }

  private Integer calculateBonuses(Order order) {
    int totalPrice = order.getOrderInfos().stream()
        .mapToInt(o -> o.getCount() * o.getDish().getPrice())
        .sum();
    return (int) Math.round(totalPrice * 0.05);
  }
}
