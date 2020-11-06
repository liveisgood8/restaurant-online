package com.ro.orders.controller;

import com.ro.auth.model.User;
import com.ro.core.exceptions.RoIllegalArgumentException;
import com.ro.core.exceptions.RoIllegalStateException;
import com.ro.orders.dto.mapper.OrderDtoMapper;
import com.ro.orders.dto.objects.OrderDto;
import com.ro.orders.lib.OrderInfo;
import com.ro.orders.model.Order;
import com.ro.orders.service.CrudOrdersService;
import com.ro.orders.service.MakingOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrdersController {
  private final MakingOrdersService makingOrdersService;
  private final CrudOrdersService crudOrdersService;
  private final OrderDtoMapper orderDtoMapper;

  @Autowired
  public OrdersController(MakingOrdersService makingOrdersService,
                          CrudOrdersService crudOrdersService,
                          OrderDtoMapper orderDtoMapper) {
    this.makingOrdersService = makingOrdersService;
    this.crudOrdersService = crudOrdersService;
    this.orderDtoMapper = orderDtoMapper;
  }

  @GetMapping
  public List<OrderDto> get(@RequestParam Boolean isApproved) {
    List<Order> orders;
    if (isApproved != null) {
      orders = crudOrdersService.getApproved(isApproved);
    } else {
      orders = crudOrdersService.getAll();
    }
    return orderDtoMapper.toDtoWithoutParts(orders);
  }

  @GetMapping("{id}")
  public OrderDto get(@PathVariable Long id) {
    Order order = crudOrdersService.getWithParts(id);
    return orderDtoMapper.toDto(order);
  }

  @PutMapping("{id}")
  public OrderDto put(@PathVariable Long id, @RequestBody OrderDto orderDto) {
    Order order = orderDtoMapper.toEntity(orderDto);
    order = crudOrdersService.update(id, order);
    return orderDtoMapper.toDto(order);
  }
  
  @PostMapping("{id}/approve")
  public void approveOrder(@PathVariable Long id) {
    makingOrdersService.approveOrder(id);
  }

  @PostMapping
  public OrderDto makeOrder(@RequestBody OrderDto orderDto, Authentication authentication) {
    User user = authentication == null ? null : (User) authentication.getPrincipal();

    Order order = makingOrdersService.save(orderDto, user);
    return orderDtoMapper.toDto(order);
  }
}
