package com.ro.orders.controller;

import com.ro.orders.validation.InsertGroup;
import com.ro.orders.model.Order;
import com.ro.orders.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrdersController {
  @Autowired
  private OrdersService ordersService;

  @PostMapping
  public void makeOrder(@RequestBody @Validated({InsertGroup.class}) Order order) {
    ordersService.makeOrder(order);
  }
}
