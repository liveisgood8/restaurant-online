package com.ro.orders.service;

import com.ro.core.data.model.Address;
import com.ro.core.data.model.TelephoneNumber;
import com.ro.core.data.repository.AddressRepository;
import com.ro.core.data.repository.TelephoneNumberRepository;
import com.ro.orders.data.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void saveOrderContacts(Order order) {
    Address address = saveOrderAddress(order.getAddress());
    TelephoneNumber telephoneNumber = saveOrderTelephoneNumber(order.getTelephoneNumber());

    order.setAddress(address);
    order.setTelephoneNumber(telephoneNumber);
  }

  private TelephoneNumber saveOrderTelephoneNumber(TelephoneNumber telephoneNumber) {
    List<TelephoneNumber> test = telephoneNumberRepository.findAll();
    if (telephoneNumber.getId() == null) {
      return telephoneNumberRepository.findByCountryCodeAndNationalNumber(
              telephoneNumber.getCountryCode(),
              telephoneNumber.getNationalNumber())
          .orElseGet(() -> telephoneNumberRepository.save(telephoneNumber));
    } else {
      return telephoneNumberRepository.save(telephoneNumber);
    }
  }

  private Address saveOrderAddress(Address address) {
    List<Address> test = addressRepository.findAll();
    if (address.getId() == null) {
      ExampleMatcher addressMatcher = ExampleMatcher.matchingAll()
          .withIgnorePaths("id")
          .withIgnoreCase();

      return addressRepository.findOne(Example.of(address, addressMatcher))
          .orElseGet(() -> addressRepository.save(address));
    } else {
      return addressRepository.save(address);
    }
  }
}
