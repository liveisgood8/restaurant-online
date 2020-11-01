package com.ro.orders.service;

import com.ro.core.exceptions.RoIllegalArgumentException;
import com.ro.core.model.Address;
import com.ro.core.model.TelephoneNumber;
import com.ro.core.repository.AddressRepository;
import com.ro.core.repository.TelephoneNumberRepository;
import com.ro.orders.model.Order;
import com.ro.orders.model.PaymentMethod;
import com.ro.orders.repository.BonusesTransactionRepository;
import com.ro.orders.repository.OrderPartsRepository;
import com.ro.orders.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CrudOrdersService {
  private final OrdersRepository ordersRepository;
  private final AddressRepository addressRepository;
  private final TelephoneNumberRepository telephoneNumberRepository;
  private final OrderPartsRepository orderPartsRepository;
  private final BonusesTransactionRepository bonusesTransactionRepository;

  @Autowired
  public CrudOrdersService(OrdersRepository ordersRepository,
                           AddressRepository addressRepository,
                           TelephoneNumberRepository telephoneNumberRepository,
                           OrderPartsRepository orderPartsRepository,
                           BonusesTransactionRepository bonusesTransactionRepository) {
    this.ordersRepository = ordersRepository;
    this.addressRepository = addressRepository;
    this.telephoneNumberRepository = telephoneNumberRepository;
    this.orderPartsRepository = orderPartsRepository;
    this.bonusesTransactionRepository = bonusesTransactionRepository;
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
  public Order update(Long id, Order order) {
    if (!order.getId().equals(id)) {
      throw new RoIllegalArgumentException("Order entity id must be the same as resource id");
    }
    return ordersRepository.save(order);
  }

}
