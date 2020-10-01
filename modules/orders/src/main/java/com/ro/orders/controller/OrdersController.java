package com.ro.orders.controller;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.ro.auth.model.User;
import com.ro.orders.controller.payloads.MakeOrderRequest;
import com.ro.orders.model.Order;
import com.ro.orders.service.OrderWithBonuses;
import com.ro.orders.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/orders")
public class OrdersController {
  @Autowired
  private OrdersService ordersService;

  @PostMapping
  public OrderWithBonuses makeOrder(@RequestBody MakeOrderRequest makeOrderRequest, Authentication authentication) {
    User user = authentication == null ? null : (User) authentication.getPrincipal();
    return ordersService.makeOrder(makeOrderRequest, user);
  }
}
