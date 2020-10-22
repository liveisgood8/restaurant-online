package com.ro.orders.controller;

import com.ro.auth.model.User;
import com.ro.orders.dto.mapper.OrderDtoMapper;
import com.ro.orders.dto.objects.MakeOrderDto;
import com.ro.orders.dto.objects.OrderDto;
import com.ro.orders.lib.OrderInfo;
import com.ro.orders.model.Order;
import com.ro.orders.service.CrudOrdersService;
import com.ro.orders.service.MakingOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrdersController {
  private final MakingOrdersService makingOrdersService;
  private final CrudOrdersService crudOrdersService;

  @Autowired
  public OrdersController(MakingOrdersService makingOrdersService,
                          CrudOrdersService crudOrdersService) {
    this.makingOrdersService = makingOrdersService;
    this.crudOrdersService = crudOrdersService;
  }

  @GetMapping
  public List<OrderDto> get(@RequestParam Boolean isApproved) {
    List<Order> orders;
    if (isApproved != null) {
      orders = crudOrdersService.getApproved(isApproved);
    } else {
      orders = crudOrdersService.getAll();
    }
    return OrderDtoMapper.INSTANCE.toDtoWithoutParts(orders);
  }

  @GetMapping("{id}")
  public OrderDto get(@PathVariable Long id) {
    Order order = crudOrdersService.getWithParts(id);
    return OrderDtoMapper.INSTANCE.toDto(order);
  }

  @PostMapping("{id}/approve")
  public void approveOrder(@PathVariable Long id) {
    makingOrdersService.approveOrder(id);
  }

  @PostMapping
  public OrderDto makeOrder(@RequestBody MakeOrderDto makeOrderDto, Authentication authentication) {
    User user = authentication == null ? null : (User) authentication.getPrincipal();
    OrderInfo orderInfo = makingOrdersService.makeOrder(makeOrderDto, user);
    return OrderDtoMapper.INSTANCE.toDto(orderInfo);
  }
}
