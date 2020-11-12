package com.ro.orders.controller;

import com.jayway.jsonpath.JsonPath;
import com.ro.auth.config.AuthModuleConfig;
import com.ro.core.CoreModuleConfig;
import com.ro.core.CoreTestUtils;
import com.ro.core.data.model.Address;
import com.ro.core.utils.TelephoneNumberUtils;
import com.ro.menu.config.MenuModuleConfig;
import com.ro.menu.dto.objects.DishDto;
import com.ro.menu.model.Dish;
import com.ro.menu.model.DishEmotion;
import com.ro.orders.config.OrdersModuleConfig;
import com.ro.orders.data.dto.objects.OrderDto;
import com.ro.orders.data.dto.objects.OrderPartDto;
import com.ro.orders.data.model.Order;
import com.ro.orders.data.model.OrderPart;
import com.ro.orders.data.repository.OrdersRepository;
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
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
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

        ResultActions resultActions = mockMvc.perform(
                get("/orders"))
                .andExpect(status().isOk());

        assertGetOrderResponseWithoutParts(resultActions, List.of(givenFirstOrder, givenSecondOrder), false);
    }

    @Test
    @WithMockUser(value = "test", authorities = "ADMIN")
    void getNonApprovedOrders() throws Exception {
        Order givenFirstOrder = orderDataTestUtil.createAndSaveOrder(true);
        Order givenSecondOrder = orderDataTestUtil.createAndSaveOrder(false);

        ResultActions resultActions = mockMvc.perform(
                get("/orders?isApproved=0"))
                .andExpect(status().isOk());

        assertGetOrderResponseWithoutParts(resultActions, List.of(givenSecondOrder), false);
    }

    @Test
    @WithMockUser(value = "test", authorities = "ADMIN")
    void getApprovedOrders() throws Exception {
        Order givenFirstOrder = orderDataTestUtil.createAndSaveOrder(true);
        Order givenSecondOrder = orderDataTestUtil.createAndSaveOrder(false);
        Order givenThirdOrder = orderDataTestUtil.createAndSaveOrder(true);

        ResultActions resultActions = mockMvc.perform(
                get("/orders?isApproved=1"))
                .andExpect(status().isOk());

        assertGetOrderResponseWithoutParts(resultActions, List.of(givenFirstOrder, givenThirdOrder), false);
    }

    @Test
    @WithMockUser(value = "test", authorities = "ADMIN")
    void getOrderById() throws Exception {
        Order givenOrder = orderDataTestUtil.createAndSaveOrder(true);

        ResultActions resultActions = mockMvc.perform(
            get("/orders/" + givenOrder.getId()))
            .andExpect(status().isOk());

        assertGetOrderResponse(resultActions, List.of(givenOrder), true);
    }

    private void assertGetOrderResponse(ResultActions resultActions, List<Order> expectedOrders,
                                        boolean singleOrderMode) throws Exception {
        assertGetOrderResponseWithoutParts(resultActions, expectedOrders, singleOrderMode);

        int i = 0;
        for (Order expectedOrder : expectedOrders) {
            Set<OrderPart> expectedOrderParts = expectedOrder.getOrderParts();
            Set<Dish> expectedOrderPartsDishes = expectedOrderParts.stream().map(OrderPart::getDish)
                    .collect(Collectors.toSet());

            String baseJsonPath = singleOrderMode ? "$." : ("$.[" + i + "].");

            resultActions
                    .andExpect(jsonPath(baseJsonPath + "orderParts.*", hasSize(expectedOrderParts.size())))
                    .andExpect(jsonPath(baseJsonPath + "orderParts[*].orderId", everyItem(equalTo(expectedOrder.getId().intValue()))))
                    .andExpect(jsonPath(baseJsonPath + "orderParts[*].count",
                            containsInAnyOrder(expectedOrderParts.stream().map(OrderPart::getCount).toArray())))
                    .andExpect(jsonPath(baseJsonPath + "orderParts[*].sellingPrice",
                            containsInAnyOrder(expectedOrderParts.stream().map(part -> part.getSellingPrice().intValue()).toArray())))
                    .andExpect(jsonPath(baseJsonPath + "orderParts[*].dish.id",
                            containsInAnyOrder(expectedOrderPartsDishes.stream().map(d -> d.getId().intValue()).toArray())))
                    .andExpect(jsonPath(baseJsonPath + "orderParts[*].dish.name",
                            containsInAnyOrder(expectedOrderPartsDishes.stream().map(Dish::getName).toArray())))
                    .andExpect(jsonPath(baseJsonPath + "orderParts[*].dish.description",
                            containsInAnyOrder(expectedOrderPartsDishes.stream().map(Dish::getDescription).toArray())))
//                  TODO Fix doubles compare
//                    .andExpect(jsonPath(baseJsonPath + "orderParts[*].dish.protein",
//                            containsInAnyOrder(expectedOrderPartsDishes.stream().map(Dish::getProtein).toArray())))
//                    .andExpect(jsonPath(baseJsonPath + "orderParts[*].dish.fat",
//                            containsInAnyOrder(expectedOrderPartsDishes.stream().map(Dish::getFat).toArray())))
//                    .andExpect(jsonPath(baseJsonPath + "orderParts[*].dish.carbohydrates",
//                            containsInAnyOrder(expectedOrderPartsDishes.stream().map(Dish::getCarbohydrates).toArray())))
                    .andExpect(jsonPath(baseJsonPath + "orderParts[*].dish.protein", notNullValue()))
                    .andExpect(jsonPath(baseJsonPath + "orderParts[*].dish.fat", notNullValue()))
                    .andExpect(jsonPath(baseJsonPath + "orderParts[*].dish.carbohydrates", notNullValue()))
                    .andExpect(jsonPath(baseJsonPath + "orderParts[*].dish.weight",
                            containsInAnyOrder(expectedOrderPartsDishes.stream().map(d -> d.getWeight().intValue()).toArray())))
                    .andExpect(jsonPath(baseJsonPath + "orderParts[*].dish.price",
                            containsInAnyOrder(expectedOrderPartsDishes.stream().map(d -> d.getPrice().intValue()).toArray())))
                    .andExpect(jsonPath(baseJsonPath + "orderParts[*].dish.imageUrl",
                            everyItem(containsString("/menu/dishes/")))) // TODO Improve assert to start with /menu/dishes/:ID/image
                    .andExpect(jsonPath(baseJsonPath + "orderParts[*].dish.likes.likeCount",
                            containsInAnyOrder(expectedOrderPartsDishes.stream().map(this::getDishLikesCount).toArray())))
                    .andExpect(jsonPath(baseJsonPath + "orderParts[*].dish.likes.dislikeCount",
                            containsInAnyOrder(expectedOrderPartsDishes.stream().map(this::getDishDislikesCount).toArray())));

            i++;
        }
    }

    private void assertGetOrderResponseWithoutParts(ResultActions resultActions, List<Order> expectedOrders,
                                                    boolean singleOrderMode) throws Exception {
        if (!singleOrderMode) {
            resultActions
                .andExpect(jsonPath("$.*", hasSize(expectedOrders.size())));
        }

        int i = 0;
        for (Order expectedOrder : expectedOrders) {
            Address expectedAddress = expectedOrder.getAddress();
            String expectedPhone = TelephoneNumberUtils.toString(expectedOrder.getTelephoneNumber());

            String baseJsonPath = singleOrderMode ? "$." : ("$.[" + i + "].");

            resultActions
                .andExpect(jsonPath(baseJsonPath + "id", equalTo(expectedOrder.getId().intValue())))
                .andExpect(jsonPath(baseJsonPath + "isApproved", equalTo(expectedOrder.getIsApproved())))
                .andExpect(jsonPath(baseJsonPath + "paymentMethod", equalTo(expectedOrder.getPaymentMethod().getName())))
                .andExpect(jsonPath(baseJsonPath + "address.id", equalTo(expectedAddress.getId().intValue())))
                .andExpect(jsonPath(baseJsonPath + "address.street", equalTo(expectedAddress.getStreet())))
                .andExpect(jsonPath(baseJsonPath + "address.homeNumber", equalTo(expectedAddress.getHomeNumber().intValue())))
                .andExpect(jsonPath(baseJsonPath + "address.entranceNumber", equalTo(expectedAddress.getEntranceNumber().intValue())))
                .andExpect(jsonPath(baseJsonPath + "address.floorNumber", equalTo(expectedAddress.getFloorNumber().intValue())))
                .andExpect(jsonPath(baseJsonPath + "address.apartmentNumber", equalTo(expectedAddress.getApartmentNumber())))
                .andExpect(jsonPath(baseJsonPath + "phone", equalTo(expectedPhone)))
                .andExpect(jsonPath(baseJsonPath + "spentBonuses", equalTo(expectedOrder.getSpentBonuses())))
                .andExpect(jsonPath(baseJsonPath + "receivedBonuses", equalTo(expectedOrder.getReceivedBonuses())))
                .andExpect(jsonPath(baseJsonPath + "totalPrice", equalTo(expectedOrder.getTotalPrice())))
                .andExpect(jsonPath(baseJsonPath + "createdAt", equalTo(expectedOrder.getCreatedAt().getTime())));

            i++;
        }
    }

    private int getDishLikesCount(Dish dish) {
        int count = 0;
        for (DishEmotion e : dish.getEmotions()) {
            if (e.getEmotionType() == DishEmotion.EmotionType.LIKE) {
                count++;
            }
        }
        return count;
    }

    private int getDishDislikesCount(Dish dish) {
        int count = 0;
        for (DishEmotion e : dish.getEmotions()) {
            if (e.getEmotionType() == DishEmotion.EmotionType.DISLIKE) {
                count++;
            }
        }
        return count;
    }

    @Test
    @WithMockUser(value = "test", authorities = "ADMIN")
    void baseUpdateOrder() throws Exception {
        Order givenOrder = orderDataTestUtil.createAndSaveOrder();

        OrderDto putOrder = orderDataTestUtil.createOrderDto(givenOrder.getId());
        ResultActions resultActions = mockMvc.perform(
                put("/orders/" + givenOrder.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CoreTestUtils.asJson(putOrder)));

        assertUpdateOrderResponse(resultActions, givenOrder, putOrder);
    }

    @Test
    @WithMockUser(value = "test", authorities = "ADMIN")
    void updateOrder_withSameOrderPartsAndPhoneAndNewAddress() throws Exception {
        Order givenOrder = orderDataTestUtil.createAndSaveOrder();

        OrderDto putOrder = createOrderDtoWithSameOrderParts(givenOrder);
        putOrder.setPhone(TelephoneNumberUtils.toString(givenOrder.getTelephoneNumber()));
        putOrder.getAddress().setId(null);

        ResultActions resultActions = mockMvc.perform(
            put("/orders/" + givenOrder.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(CoreTestUtils.asJson(putOrder)));

        assertUpdateOrderResponse(resultActions, givenOrder, putOrder);
    }

    private OrderDto createOrderDtoWithSameOrderParts(Order partsSource) {
        OrderDto orderDto = orderDataTestUtil.createOrderDto(partsSource.getId());

        Set<OrderPartDto> putOrderParts = new HashSet<>();
        for (OrderPart orderPart : partsSource.getOrderParts()) {
            DishDto dishDto = new DishDto();
            dishDto.setId(orderPart.getDish().getId());
            dishDto.setName(orderPart.getDish().getName());
            dishDto.setDescription(orderPart.getDish().getDescription());
            dishDto.setProtein(orderPart.getDish().getProtein());
            dishDto.setFat(orderPart.getDish().getFat());
            dishDto.setCarbohydrates(orderPart.getDish().getCarbohydrates());
            dishDto.setWeight(orderPart.getDish().getWeight());
            dishDto.setProtein(orderPart.getDish().getProtein());
            dishDto.setPrice(orderPart.getDish().getPrice());

            OrderPartDto orderPartDto = new OrderPartDto();
            orderPartDto.setDish(dishDto);
            orderPartDto.setOrderId(orderPart.getId().getOrderId());
            orderPartDto.setSellingPrice(orderPart.getSellingPrice());
            orderPartDto.setCount(orderPart.getCount());

            putOrderParts.add(orderPartDto);
        }
        orderDto.setOrderParts(putOrderParts);

        return orderDto;
    }

    @Test
    @WithMockUser(value = "test", authorities = "ADMIN")
    void updateOrder_whenOrderPartSizeChanged() throws Exception {
        Order givenOrder = orderDataTestUtil.createAndSaveOrder();

        OrderDto putOrder = orderDataTestUtil.createOrderDto(givenOrder.getId());
        putOrder.setOrderParts(Set.of(putOrder.getOrderParts().iterator().next()));

        ResultActions resultActions = mockMvc.perform(
                put("/orders/" + givenOrder.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CoreTestUtils.asJson(putOrder)));

        assertUpdateOrderResponse(resultActions, givenOrder, putOrder);
    }

    private void assertUpdateOrderResponse(ResultActions resultActions, Order givenOrder, OrderDto expectedOrder)
            throws Exception {
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(givenOrder.getId().intValue())))
                .andExpect(jsonPath("$.isApproved", equalTo(expectedOrder.getIsApproved())))
                .andExpect(jsonPath("$.phone", equalTo(expectedOrder.getPhone())))
                .andExpect(jsonPath("$.totalPrice", equalTo(orderDataTestUtil.getOrderDtoTotalPrice(expectedOrder))))
                .andExpect(jsonPath("$.paymentMethod", equalTo(expectedOrder.getPaymentMethod())))
                .andExpect(jsonPath("$.createdAt", equalTo(givenOrder.getCreatedAt().getTime())))
                .andExpect(jsonPath("$.address.id", notNullValue()))
                .andExpect(jsonPath("$.address.street", equalTo(expectedOrder.getAddress().getStreet())))
                .andExpect(jsonPath("$.address.homeNumber", equalTo(expectedOrder.getAddress().getHomeNumber().intValue())))
                .andExpect(jsonPath("$.address.entranceNumber", equalTo(expectedOrder.getAddress().getEntranceNumber().intValue())))
                .andExpect(jsonPath("$.address.floorNumber", equalTo(expectedOrder.getAddress().getFloorNumber().intValue())))
                .andExpect(jsonPath("$.address.apartmentNumber", equalTo(expectedOrder.getAddress().getApartmentNumber())))
                .andExpect(jsonPath("$.orderParts.*", hasSize(expectedOrder.getOrderParts().size())))
                .andExpect(jsonPath("$.orderParts[*].orderId", everyItem(equalTo(givenOrder.getId().intValue()))))
                .andExpect(jsonPath("$.orderParts[*].dish.id",
                        containsInAnyOrder(expectedOrder.getOrderParts().stream().map(op -> op.getDish().getId().intValue()).toArray())))
                .andExpect(jsonPath("$.orderParts[*].dish.name",
                        containsInAnyOrder(expectedOrder.getOrderParts().stream().map(op -> op.getDish().getName()).toArray())))
                .andExpect(jsonPath("$.orderParts[*].dish.price",
                        containsInAnyOrder(expectedOrder.getOrderParts().stream().map(op -> op.getDish().getPrice().intValue()).toArray())))
                .andExpect(jsonPath("$.orderParts[*].count",
                        containsInAnyOrder(expectedOrder.getOrderParts().stream().map(OrderPartDto::getCount).toArray())))
                .andExpect(jsonPath("$.orderParts[*].sellingPrice",
                        containsInAnyOrder(expectedOrder.getOrderParts().stream().map(part -> part.getSellingPrice().intValue()).toArray())));
    }
}
