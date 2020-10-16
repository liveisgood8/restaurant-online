package com.ro.orders.controller;

import com.ro.auth.model.User;
import com.ro.orders.dto.mapper.OrderDtoMapper;
import com.ro.orders.dto.objects.OrderDto;
import com.ro.orders.lib.OrderInfo;
import com.ro.orders.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
  public OrderDto makeOrder(@RequestBody OrderDto orderDto, Authentication authentication) {
    User user = authentication == null ? null : (User) authentication.getPrincipal();
    OrderInfo orderInfo = ordersService.makeOrder(orderDto, user);
    return OrderDtoMapper.INSTANCE.toDto(orderInfo);
  }
}
