package com.ro.orders.controller;

import com.ro.auth.model.User;
import com.ro.orders.controller.payloads.MakeOrderRequest;
import com.ro.orders.dto.mapper.OrderWithBonusesDtoMapper;
import com.ro.orders.dto.objects.OrderWithBonusesDto;
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
  public OrderWithBonusesDto makeOrder(@RequestBody MakeOrderRequest makeOrderRequest, Authentication authentication) {
    User user = authentication == null ? null : (User) authentication.getPrincipal();
    OrderWithBonuses orderWithBonuses = ordersService.makeOrder(makeOrderRequest, user);
    return OrderWithBonusesDtoMapper.INSTANCE.toDto(orderWithBonuses);
  }
}
