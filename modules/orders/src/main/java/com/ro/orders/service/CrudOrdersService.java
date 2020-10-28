package com.ro.orders.service;

import com.ro.menu.repository.DishRepository;
import com.ro.orders.dto.mapper.OrderDtoMapper;
import com.ro.orders.dto.objects.OrderDto;
import com.ro.orders.model.Order;
import com.ro.orders.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class CrudOrdersService {
  private final OrdersRepository ordersRepository;
  private final DishRepository dishRepository;
  private final OrderDtoMapper orderDtoMapper;

  @Autowired
  public CrudOrdersService(OrdersRepository ordersRepository,
                           DishRepository dishRepository,
                           OrderDtoMapper orderDtoMapper) {
    this.ordersRepository = ordersRepository;
    this.dishRepository = dishRepository;
    this.orderDtoMapper = orderDtoMapper;
  }

  public List<Order> getAll() {
    return ordersRepository.findAll();
  }

  public List<Order> getApproved(boolean isApproved) {
    return ordersRepository.findByIsApproved(isApproved);
  }

  public Order getWithParts(Long id) {
    return ordersRepository.findWithOrderPartsById(id)
        .orElseThrow(() -> new EntityNotFoundException("Order with id: " + id + " not founded"));
  }

  @Transactional
  public Order update(Long id, OrderDto newOrderDto) {
    Order newOrder = orderDtoMapper.toEntity(newOrderDto);
    Order order = ordersRepository.findWithPartsById(newOrder.getId())
        .orElseThrow(() -> new EntityNotFoundException("Order with id: " + id + " is not founded"));

//    order.setAddress(newOrder.getAddress());
//    order.set
//    order.getOrderParts().forEach(p -> {
//      Dish dish = dishRepository.findById(p.getDish().getId())
//          .orElseThrow(() -> new EntityNotFoundException("Dish with id: " + p.getDish().getId() + " is not founded"));
//      p.setDish(dish);
//      p.setOrder(order);
//    });

    return order;
  }
}
