package com.restaurantonline.ordersservice.controller;

import com.restaurantonline.ordersservice.validation.InsertGroup;
import com.restaurantonline.ordersservice.model.Order;
import com.restaurantonline.ordersservice.service.OrdersService;
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
