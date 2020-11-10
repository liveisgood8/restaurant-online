package com.ro.analytics.service;

import com.ro.analytics.data.lib.DishOrderStatistic;
import com.ro.core.CoreTestUtils;
import com.ro.menu.model.Dish;
import com.ro.orders.data.model.Order;
import com.ro.orders.data.model.OrderPart;
import com.ro.orders.data.repository.OrdersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class OrderAnalyticsServiceTest {

  @Mock
  private OrdersRepository ordersRepository;

  private OrderAnalyticsService service;

  @BeforeEach
  void setUp() {
    service = new OrderAnalyticsService(ordersRepository);
  }

  @Test
  void getStatisticByDishes_whenTop3() {
    List<Order> givenOrders = mockFindAllApprovedOrders(6);
    DishOrderStatistic maxOrders = getMaxOrders(givenOrders);
    DishOrderStatistic givenTop1 = getTopDish(givenOrders, 1);
    DishOrderStatistic givenTop2 = getTopDish(givenOrders, 2);
    DishOrderStatistic givenTop3 = getTopDish(givenOrders, 3);

    List<DishOrderStatistic> actualDishOrderStatistics = service.getStatisticByDishes(3);

    assertEquals(3, actualDishOrderStatistics.size());

    assertEquals(maxOrders.getDish().getId(), givenTop1.getDish().getId());
    assertEquals(maxOrders.getNumberOfOrders(), givenTop1.getNumberOfOrders());

    assertEquals(givenTop1.getDish().getId(), actualDishOrderStatistics.get(0).getDish().getId());
    assertEquals(givenTop1.getNumberOfOrders(), actualDishOrderStatistics.get(0).getNumberOfOrders());

    assertEquals(givenTop2.getNumberOfOrders(), actualDishOrderStatistics.get(1).getNumberOfOrders());
    assertEquals(givenTop2.getNumberOfOrders(), actualDishOrderStatistics.get(1).getNumberOfOrders());

    assertEquals(givenTop3.getNumberOfOrders(), actualDishOrderStatistics.get(2).getNumberOfOrders());
    assertEquals(givenTop3.getNumberOfOrders(), actualDishOrderStatistics.get(2).getNumberOfOrders());

    assertTrue(givenTop1.getNumberOfOrders() > givenTop2.getNumberOfOrders());
    assertTrue(givenTop2.getNumberOfOrders() > givenTop3.getNumberOfOrders());
  }

  @Test
  void getStatisticByDishes_whenTop1() {
    List<Order> givenOrders = mockFindAllApprovedOrders(6);
    DishOrderStatistic maxOrders = getMaxOrders(givenOrders);
    DishOrderStatistic givenTop1 = getTopDish(givenOrders, 1);
    DishOrderStatistic givenTop2 = getTopDish(givenOrders, 2);

    List<DishOrderStatistic> actualDishOrderStatistics = service.getStatisticByDishes(1);

    assertEquals(1, actualDishOrderStatistics.size());

    assertEquals(maxOrders.getDish().getId(), givenTop1.getDish().getId());
    assertEquals(maxOrders.getNumberOfOrders(), givenTop1.getNumberOfOrders());

    assertEquals(givenTop1.getDish().getId(), actualDishOrderStatistics.get(0).getDish().getId());
    assertEquals(givenTop1.getNumberOfOrders(), actualDishOrderStatistics.get(0).getNumberOfOrders());

    assertTrue(givenTop1.getNumberOfOrders() > givenTop2.getNumberOfOrders());
  }

  private List<Order> mockFindAllApprovedOrders(int dishPoolSize) {
    List<Dish> dishesPool = makeDishPool(dishPoolSize);
    List<Order> randomOrders = new ArrayList<>();
    Random random = new Random();
    for (int i = 0; i < 500; i++) {
      Order order = CoreTestUtils.getRandomObject(Order.class);
      for (int j = 0; j < 5; j++) {
        OrderPart orderPart = CoreTestUtils.getRandomObject(OrderPart.class);
        orderPart.setDish(dishesPool.get(random.nextInt(dishPoolSize)));
        order.getOrderParts().add(orderPart);
      }
      randomOrders.add(order);
    }

    Mockito.when(ordersRepository.findAllApprovedWithFullyFetchedParts()).thenReturn(randomOrders);
    return randomOrders;
  }

  private List<Dish> makeDishPool(int poolSize) {
    List<Dish> pool = new ArrayList<>();
    for (int i = 0; i < 500; i++) {
      Dish dish = CoreTestUtils.getRandomObject(Dish.class);
      pool.add(dish);
    }
    return pool;
  }

  private DishOrderStatistic getTopDish(List<Order> orders, int topPosition) {
    return rangeByDishesOrderCount(orders)
        .entrySet()
        .stream()
        .sorted(Map.Entry.comparingByValue((a, b) -> Integer.compare(b, a)))
        .skip(topPosition - 1)
        .findFirst()
        .map(e -> new DishOrderStatistic(e.getKey(), e.getValue()))
        .orElseThrow();
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

  private DishOrderStatistic getMaxOrders(List<Order> orders) {
    return rangeByDishesOrderCount(orders)
        .entrySet()
        .stream()
        .max(Map.Entry.comparingByValue())
        .map(e -> new DishOrderStatistic(e.getKey(), e.getValue()))
        .orElseThrow();
  }


}