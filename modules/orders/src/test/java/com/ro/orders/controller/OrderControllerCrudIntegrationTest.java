package com.ro.orders.controller;

import com.ro.auth.config.AuthModuleConfig;
import com.ro.core.CoreModuleConfig;
import com.ro.menu.config.MenuModuleConfig;
import com.ro.orders.config.OrdersServiceConfig;
import com.ro.orders.dto.objects.OrderDto;
import com.ro.orders.model.Order;
import com.ro.orders.repository.OrdersRepository;
import com.ro.orders.service.CrudOrdersService;
import com.ro.orders.utils.OrderDataTestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {
        OrdersServiceConfig.class,
        MenuModuleConfig.class,
        CoreModuleConfig.class,
        AuthModuleConfig.class
})
public class OrderControllerCrudIntegrationTest extends AbstractControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private OrderDataTestUtil orderDataTestUtil;

    @Test
    void updateOrder() throws Exception {
        Order givenOrder = orderDataTestUtil.createAndSaveOrder();
        OrderDto updateOrder = orderDataTestUtil.createOrderDto();

        mockMvc.perform(
                put("/order/" + givenOrder.getId())
                        .content(asJson(updateOrder)));

        // TODO Assertation
    }
}
