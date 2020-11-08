package com.ro.orders.controller;

import com.ro.auth.AuthTestUtils;
import com.ro.auth.config.AuthModuleConfig;
import com.ro.core.CoreModuleConfig;
import com.ro.core.CoreTestUtils;
import com.ro.core.utils.TelephoneNumberUtils;
import com.ro.menu.config.MenuModuleConfig;
import com.ro.menu.repository.CategoryRepository;
import com.ro.menu.repository.DishRepository;
import com.ro.orders.config.OrdersModuleConfig;
import com.ro.orders.data.dto.objects.OrderDto;
import com.ro.orders.data.dto.objects.OrderPartDto;
import com.ro.orders.data.model.Order;
import com.ro.orders.data.model.PaymentMethod;
import com.ro.orders.utils.OrderDataTestUtil;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.MatcherAssert.*;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {
    OrdersModuleConfig.class,
    MenuModuleConfig.class,
    CoreModuleConfig.class,
    AuthModuleConfig.class,
    AuthTestUtils.class,
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class OrdersControllerMakingOrderIntegrationTest {

  @Autowired
  private DishRepository dishRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  @Autowired
  private OrderDataTestUtil orderDataTestUtil;

  @Autowired
  private AuthTestUtils authTestUtils;

  @Autowired
  private MockMvc mockMvc;

  @Test
  void baseMakeOrderByCash_whenUnauthorized() throws Exception {
    OrderDto givenOrder = createOrderDtoForMakeOrder(PaymentMethod.BY_CASH_TO_THE_COURIER);
    givenOrder.setIsApproved(true); // Must be ignored in service layer

    ResultActions result = mockMvc.perform(
        post("/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(CoreTestUtils.asJson(givenOrder)))
        .andExpect(jsonPath("$.spentBonuses", is(0)))
        .andExpect(jsonPath("$.receivedBonuses", is(0)));

    assertMakeOrderAnswer(givenOrder, false, result);
    assertOrderInDatabase(givenOrder, false, false);
  }

  @Test
  public void makeOrderByCardOnline_whenUnauthorized_thenMustBeApproved() throws Exception {
    OrderDto givenOrder = createOrderDtoForMakeOrder(PaymentMethod.BY_CARD_ONLINE);

    ResultActions result = mockMvc.perform(
        post("/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(CoreTestUtils.asJson(givenOrder)))
        .andExpect(jsonPath("$.spentBonuses", is(0)))
        .andExpect(jsonPath("$.receivedBonuses", is(0)));

    assertMakeOrderAnswer(givenOrder, true, result);
    assertOrderInDatabase(givenOrder, true,false);
  }

  @Test
  public void makeOrder_whenAuthorized() throws Exception {
    OrderDto givenOrder = createOrderDtoForMakeOrder(PaymentMethod.BY_CARD_ONLINE);
    givenOrder.setSpentBonuses(59);
    givenOrder.setReceivedBonuses(484); // Must be ignored

    ResultActions result = mockMvc.perform(
        post("/orders")
            .with(user(authTestUtils.createAndSaveUserInDataSource()))
            .contentType(MediaType.APPLICATION_JSON)
            .content(CoreTestUtils.asJson(givenOrder)))
        .andExpect(jsonPath("$.spentBonuses", is(givenOrder.getSpentBonuses())))
        .andExpect(jsonPath("$.receivedBonuses", is(getExpectedReceivedBonuses(givenOrder))));

    assertMakeOrderAnswer(givenOrder, true, result);
    assertOrderInDatabase(givenOrder, true,true);
  }

  private OrderDto createOrderDtoForMakeOrder(String paymentMethodName) {
    OrderDto orderDto = orderDataTestUtil.createOrderDto(null);
    orderDto.setPaymentMethod(paymentMethodName);

    return orderDto;
  }

  private void assertMakeOrderAnswer(OrderDto givenOrder,
                                     boolean expectedApprovedState,
                                     ResultActions resultActions) throws Exception {
      resultActions
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.isApproved", is(expectedApprovedState)))
        .andExpect(jsonPath("$.paymentMethod", is(givenOrder.getPaymentMethod())))
        .andExpect(jsonPath("$.phone", is(givenOrder.getPhone())))
        .andExpect(jsonPath("$.orderParts[*]", hasSize(2)))
        .andExpect(jsonPath("$.orderParts[*].dish.id", containsInAnyOrder(givenOrder.getOrderParts()
                .stream().map(p -> p.getDish().getId().intValue()).toArray())))
        .andExpect(jsonPath("$.orderParts[*].dish.name", containsInAnyOrder(givenOrder.getOrderParts()
            .stream().map(p -> p.getDish().getName()).toArray())))
        .andExpect(jsonPath("$.orderParts[*].sellingPrice", containsInAnyOrder(givenOrder.getOrderParts()
                .stream().map(p -> p.getSellingPrice().intValue()).toArray())))
        .andExpect(jsonPath("$.orderParts[*].count", containsInAnyOrder(givenOrder.getOrderParts()
            .stream().map(OrderPartDto::getCount).toArray())))
        .andExpect(jsonPath("$.createdAt", notNullValue()));
  }

  private void assertOrderInDatabase(OrderDto givenOrder,
                                     boolean expectedApprovedState,
                                     boolean shouldBonusesExist) {
    Order order = orderDataTestUtil.getFullyInitialized(1L);

    assertEquals(givenOrder.getPaymentMethod(), order.getPaymentMethod().getName());
    assertEquals(expectedApprovedState, order.getIsApproved());
    if (!shouldBonusesExist) {
      assertEquals(0, order.getReceivedBonuses());
      assertEquals(0, order.getSpentBonuses());
    } else {
      int expectedReceivedBonuses = getExpectedReceivedBonuses(givenOrder);

      assertEquals(expectedReceivedBonuses, order.getReceivedBonuses());
      assertEquals(givenOrder.getSpentBonuses(), order.getSpentBonuses());
    }

    // Phone assertation
    assertEquals(givenOrder.getPhone(), TelephoneNumberUtils.toString(order.getTelephoneNumber()));

    // Address assertation
    assertEquals(givenOrder.getAddress().getApartmentNumber(), order.getAddress().getApartmentNumber());
    assertEquals(givenOrder.getAddress().getEntranceNumber(), order.getAddress().getEntranceNumber());
    assertEquals(givenOrder.getAddress().getFloorNumber(), order.getAddress().getFloorNumber());
    assertEquals(givenOrder.getAddress().getHomeNumber(), order.getAddress().getHomeNumber());
    assertEquals(givenOrder.getAddress().getStreet(), order.getAddress().getStreet());

    // OrderParts assertation
    assertEquals(givenOrder.getOrderParts().size(), order.getOrderParts().size());
    assertThat(order.getOrderParts(), hasItem(
        hasProperty("dish",
            hasProperty("name",
                anyOf(givenOrder.getOrderParts().stream().map(p -> CoreMatchers.equalTo(p.getDish().getName()))
                    .collect(Collectors.toSet()))))));
    assertThat(order.getOrderParts(), hasItem(
        hasProperty("count",
            anyOf(givenOrder.getOrderParts().stream().map(p -> CoreMatchers.equalTo(p.getCount()))
                .collect(Collectors.toSet())))));
  }

  private int getExpectedReceivedBonuses(OrderDto givenOrder) {
    int givenTotalPrice = givenOrder.getOrderParts()
        .stream()
        .mapToInt(p -> p.getDish().getPrice() * p.getCount())
        .sum();

    givenTotalPrice -= givenOrder.getSpentBonuses();

    return (int) Math.round(givenTotalPrice * 0.05);
  }

}