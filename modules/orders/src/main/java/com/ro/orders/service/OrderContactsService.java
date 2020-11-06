package com.ro.orders.service;

import com.ro.core.model.Address;
import com.ro.core.model.TelephoneNumber;
import com.ro.core.repository.AddressRepository;
import com.ro.core.repository.TelephoneNumberRepository;
import com.ro.orders.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class OrderContactsService {
  private final TelephoneNumberRepository telephoneNumberRepository;
  private final AddressRepository addressRepository;

  @Autowired
  public OrderContactsService(TelephoneNumberRepository telephoneNumberRepository,
                              AddressRepository addressRepository) {
    this.telephoneNumberRepository = telephoneNumberRepository;
    this.addressRepository = addressRepository;
  }

  public void saveOrderContacts(Order order) {
    saveOrderAddress(order);
    saveOrderTelephoneNumber(order);
  }

  private void saveOrderTelephoneNumber(Order order) {
    if (order.getTelephoneNumber().getId() == null) {
      TelephoneNumber newTelephoneNumber = telephoneNumberRepository.findByCountryCodeAndNationalNumber(
          order.getTelephoneNumber().getCountryCode(),
          order.getTelephoneNumber().getNationalNumber())
          .orElse(telephoneNumberRepository.save(order.getTelephoneNumber()));

      order.setTelephoneNumber(newTelephoneNumber);
    } else {
      order.setTelephoneNumber(telephoneNumberRepository.save(order.getTelephoneNumber()));
    }
  }

  private void saveOrderAddress(Order order) {
    if (order.getAddress().getId() == null) {
      ExampleMatcher addressMatcher = ExampleMatcher.matchingAll()
          .withIgnorePaths("id")
          .withIgnoreCase();

      Address address = addressRepository.findOne(Example.of(order.getAddress(), addressMatcher))
          .orElse(addressRepository.save(order.getAddress()));

      order.setAddress(address);
    } else {
      order.setAddress(addressRepository.save(order.getAddress()));
    }
  }
}
