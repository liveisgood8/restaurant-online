package com.ro.orders.data.repository;

import com.ro.auth.config.AuthModuleConfig;
import com.ro.core.CoreModuleConfig;
import com.ro.core.CoreTestUtils;
import com.ro.core.data.model.Address;
import com.ro.core.data.model.TelephoneNumber;
import com.ro.core.data.repository.AddressRepository;
import com.ro.core.data.repository.TelephoneNumberRepository;
import com.ro.menu.config.MenuModuleConfig;
import com.ro.menu.model.Category;
import com.ro.menu.model.Dish;
import com.ro.orders.config.OrdersModuleConfig;
import com.ro.orders.data.model.Order;
import com.ro.orders.data.model.OrderPart;
import com.ro.orders.data.model.PaymentMethod;
import com.ro.orders.utils.OrderDataTestUtil;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({OrdersModuleConfig.class, MenuModuleConfig.class, CoreModuleConfig.class, AuthModuleConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class OrdersRepositoryTest {

  @Autowired
  private OrdersRepository ordersRepository;

  @Autowired
  private OrderDataTestUtil orderDataTestUtil;

  @Test
  void findByIsApproved_whenApproved() {
    Order firstGivenOrder = createAndSaveOrder(true);
    createAndSaveOrder(false);
    createAndSaveOrder(false);

    List<Order> actualApprovedOrders = ordersRepository.findByIsApproved(true);
    assertEquals(1, actualApprovedOrders.size());
    assertEquals(firstGivenOrder.getId(), actualApprovedOrders.get(0).getId());
  }

  @Test
  void findByIsApproved_whenNonApproved() {
    Order firstGivenOrder = createAndSaveOrder(false);
    Order secondGivenOrder = createAndSaveOrder(false);
    createAndSaveOrder(true);

    List<Order> actualApprovedOrders = ordersRepository.findByIsApproved(false);

    assertEquals(2, actualApprovedOrders.size());
    assertEquals(firstGivenOrder.getId(), actualApprovedOrders.get(0).getId());
    assertEquals(secondGivenOrder.getId(), actualApprovedOrders.get(1).getId());
  }

  @Test
  @Transactional(propagation = Propagation.NEVER)
  void findAllApprovedWithFullyFetchedParts() {
    Order givenOrder = createAndSaveOrder(true);
    createAndSaveOrder(false);

    List<Order> actualOrders = ordersRepository.findAllApprovedWithFullyFetchedParts();

    assertEquals(1, actualOrders.size());
    assertEquals(givenOrder.getId(), actualOrders.get(0).getId());
  }

  @Test
  @Transactional(propagation = Propagation.NEVER)
  void findWithOrderPartsById() {
    Order givenOrder = createAndSaveOrder(false);

    Order actualOrder = ordersRepository.findWithOrderPartsById(givenOrder.getId()).orElseThrow();

    assertEquals(givenOrder.getId(), actualOrder.getId());
    assertEquals(givenOrder.getOrderParts(), actualOrder.getOrderParts());
  }

  private Order createAndSaveOrder(boolean isApproved) {
    return orderDataTestUtil.createAndSaveOrder(isApproved);
  }
}