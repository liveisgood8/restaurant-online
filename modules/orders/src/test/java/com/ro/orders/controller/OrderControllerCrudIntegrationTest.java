package com.ro.orders.controller;

import com.ro.auth.config.AuthModuleConfig;
import com.ro.core.CoreModuleConfig;
import com.ro.core.CoreTestUtils;
import com.ro.menu.config.MenuModuleConfig;
import com.ro.orders.config.OrdersModuleConfig;
import com.ro.orders.dto.objects.OrderDto;
import com.ro.orders.dto.objects.OrderPartDto;
import com.ro.orders.model.Order;
import com.ro.orders.repository.OrdersRepository;
import com.ro.orders.utils.OrderDataTestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {
        OrdersModuleConfig.class,
        MenuModuleConfig.class,
        CoreModuleConfig.class,
        AuthModuleConfig.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class OrderControllerCrudIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private OrderDataTestUtil orderDataTestUtil;

    @Test
    @WithMockUser(value = "test", authorities = "ADMIN")
    void baseGetAllOrders() throws Exception {
        Order givenFirstOrder = orderDataTestUtil.createAndSaveOrder(true);
        Order givenSecondOrder = orderDataTestUtil.createAndSaveOrder(false);

        mockMvc.perform(
            get("/orders"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.*", hasSize(2)));
        // TODO Order asserts
    }

    @Test
    @WithMockUser(value = "test", authorities = "ADMIN")
    void getNonApprovedOrders() throws Exception {
        Order givenFirstOrder = orderDataTestUtil.createAndSaveOrder(true);
        Order givenSecondOrder = orderDataTestUtil.createAndSaveOrder(false);

        mockMvc.perform(
            get("/orders?isApproved=0"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.*", hasSize(1)));
        // TODO Order asserts
    }

    @Test
    @WithMockUser(value = "test", authorities = "ADMIN")
    void updateOrder() throws Exception {
        Order givenOrder = orderDataTestUtil.createAndSaveOrder();

        OrderDto putOrder = orderDataTestUtil.createOrderDto(givenOrder.getId());
        mockMvc.perform(
                put("/orders/" + givenOrder.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CoreTestUtils.asJson(putOrder)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", equalTo(givenOrder.getId().intValue())))
            .andExpect(jsonPath("$.isApproved", equalTo(putOrder.getIsApproved())))
            .andExpect(jsonPath("$.phone", equalTo(putOrder.getPhone())))
            .andExpect(jsonPath("$.totalPrice", equalTo(orderDataTestUtil.getOrderDtoTotalPrice(putOrder))))
            .andExpect(jsonPath("$.paymentMethod", equalTo(putOrder.getPaymentMethod())))
            .andExpect(jsonPath("$.createdAt", equalTo(givenOrder.getCreatedAt().getTime())))
            .andExpect(jsonPath("$.address.id", notNullValue()))
            .andExpect(jsonPath("$.address.street", equalTo(putOrder.getAddress().getStreet())))
            .andExpect(jsonPath("$.address.homeNumber", equalTo(putOrder.getAddress().getHomeNumber().intValue())))
            .andExpect(jsonPath("$.address.entranceNumber", equalTo(putOrder.getAddress().getEntranceNumber().intValue())))
            .andExpect(jsonPath("$.address.floorNumber", equalTo(putOrder.getAddress().getFloorNumber().intValue())))
            .andExpect(jsonPath("$.address.apartmentNumber", equalTo(putOrder.getAddress().getApartmentNumber())))
            .andExpect(jsonPath("$.orderParts[*].orderId", everyItem(equalTo(givenOrder.getId().intValue()))))
            .andExpect(jsonPath("$.orderParts[*].dish.id",
                containsInAnyOrder(putOrder.getOrderParts().stream().map(op -> op.getDish().getId().intValue()).toArray())))
            .andExpect(jsonPath("$.orderParts[*].count",
                containsInAnyOrder(putOrder.getOrderParts().stream().map(OrderPartDto::getCount).toArray())))
            .andExpect(jsonPath("$.orderParts[*].totalPrice",
                containsInAnyOrder(putOrder.getOrderParts().stream().map(orderDataTestUtil::getOrderPartDtoTotalPrice).toArray())));
    }
}
