package com.ro.orders.dto.mapper;

import com.ro.menu.dto.mappers.DishDtoMapper;
import com.ro.menu.dto.objects.DishDto;
import com.ro.menu.model.Dish;
import com.ro.menu.repository.DishRepository;
import com.ro.orders.dto.objects.OrderPartDto;
import com.ro.orders.model.Order;
import com.ro.orders.model.OrderPart;
import com.ro.orders.repository.OrdersRepository;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;

@Mapper(uses = {DishDtoMapper.class}, componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class OrderPartDtoMapper {
  private DishRepository dishRepository;
  private OrdersRepository ordersRepository;

  @Autowired
  public void setDishRepository(DishRepository dishRepository) {
    this.dishRepository = dishRepository;
  }

  @Autowired
  public void setOrdersRepository(OrdersRepository ordersRepository) {
    this.ordersRepository = ordersRepository;
  }

  @Mapping(target = "orderId", source = "id.orderId")
  public abstract OrderPartDto toDto(OrderPart orderPart);

  @Mapping(target = "id.dishId", source = "dish.id")
  @Mapping(target = "id.orderId", source = "orderId")
  @Mapping(target = "order", source = "orderId", qualifiedByName = "orderIdToOrder")
  @Mapping(target = "totalPrice", source = ".", qualifiedByName = "entityTotalPrice")
  public abstract OrderPart toEntity(OrderPartDto orderPartDto);

  @Named("entityTotalPrice")
  protected Integer entityTotalPrice(OrderPartDto part) {
    return part.getDish().getPrice() * part.getCount();
  }

  protected Dish dishDtoToDish(DishDto dishDto) {
    return dishRepository.findById(dishDto.getId())
        .orElseThrow(() -> new EntityNotFoundException("Dish with id: " + dishDto.getId() + " is not founded"));
  }

  @Named("orderIdToOrder")
  protected Order orderIdToOrder(Long orderId) {
    if (orderId == null) {
      return null;
    }

    return ordersRepository.findById(orderId)
        .orElseThrow(() -> new EntityNotFoundException("Order with id: " + orderId + " is not founded"));
  }
}
