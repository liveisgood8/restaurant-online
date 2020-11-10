package com.ro.analytics.service;

import com.ro.analytics.data.lib.DishOrderStatistic;
import com.ro.menu.model.Dish;
import com.ro.orders.data.model.Order;
import com.ro.orders.data.model.OrderPart;
import com.ro.orders.data.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderAnalyticsService {
  private final OrdersRepository ordersRepository;

  @Autowired
  public OrderAnalyticsService(OrdersRepository ordersRepository) {
    this.ordersRepository = ordersRepository;
  }

  public List<DishOrderStatistic> getStatisticByDishes(int topCount) {
    List<Order> orders = ordersRepository.findAllApprovedWithFullyFetchedParts();
    Map<Dish, Integer> dishesOrdersCount = rangeByDishesOrderCount(orders);

    return dishesOrdersCount
        .entrySet()
        .stream()
        .sorted(Map.Entry.comparingByValue((a, b) -> Integer.compare(b, a)))
        .limit(topCount)
        .map(entry -> new DishOrderStatistic(entry.getKey(), entry.getValue()))
        .collect(Collectors.toList());
  }

  private Map<Dish, Integer> rangeByDishesOrderCount(List<Order> orders) {
    Map<Dish, Integer> dishOrdersCount = new HashMap<>();

    for (Order order : orders) {
      for (OrderPart part : order.getOrderParts()) {
        Integer currentCount = dishOrdersCount.get(part.getDish());
        if (currentCount != null) {
          dishOrdersCount.put(part.getDish(), ++currentCount);
        } else {
          dishOrdersCount.put(part.getDish(), 1);
        }
      }
    }

    return dishOrdersCount;
  }
}
