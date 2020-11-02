package com.ro.orders.utils;

import com.ro.core.model.Address;
import com.ro.core.model.TelephoneNumber;
import com.ro.core.repository.AddressRepository;
import com.ro.core.repository.TelephoneNumberRepository;
import com.ro.menu.model.Category;
import com.ro.menu.model.Dish;
import com.ro.menu.repository.CategoryRepository;
import com.ro.menu.repository.DishRepository;
import com.ro.orders.dto.objects.OrderDto;
import com.ro.orders.dto.objects.OrderPartDto;
import com.ro.orders.model.Order;
import com.ro.orders.model.OrderPart;
import com.ro.orders.model.PaymentMethod;
import com.ro.orders.repository.OrdersRepository;
import com.ro.orders.repository.PaymentMethodRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Set;

@Component
public class OrderDataTestUtil {
    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private TelephoneNumberRepository telephoneNumberRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Transactional
    public Order getFullyInitialized(Long id) {
        Order order = ordersRepository.findById(id).orElseThrow();
        initializeOrderLazyProperties(order);

        return order;
    }

    private void initializeOrderLazyProperties(Order order) {
        Hibernate.initialize(order.getAddress());
        Hibernate.initialize(order.getBonusesTransactions());
        Hibernate.initialize(order.getOrderParts());
        order.getOrderParts().forEach(p -> {
            Hibernate.initialize(p.getDish());
        });
    }

    public Order createAndSaveOrder() {
        Order order = ObjectGenerator.getRandomObject(Order.class);
        order.setPaymentMethod(paymentMethodRepository.findByName(PaymentMethod.BY_CARD_ONLINE).orElseThrow());
        order.setAddress(createAndSaveAddress());
        order.setTelephoneNumber(createAndSaveTelephoneNumber());
        order.setUser(null);

        OrderPart firstPart = ObjectGenerator.getRandomObject(OrderPart.class);
        firstPart.setDish(createAndSaveDish());

        OrderPart secondPart = ObjectGenerator.getRandomObject(OrderPart.class);
        secondPart.setDish(createAndSaveDish());

        order = ordersRepository.save(order);
        initializeOrderLazyProperties(order);

        return order;
    }

    private TelephoneNumber createAndSaveTelephoneNumber() {
        TelephoneNumber telephoneNumber = new TelephoneNumber();
        telephoneNumber.setCountryCode("7");
        telephoneNumber.setNationalNumber("9612654777");

        return telephoneNumberRepository.save(telephoneNumber);
    }

    private Address createAndSaveAddress() {
        Address address = ObjectGenerator.getRandomObject(Address.class);
        return addressRepository.save(address);
    }

    public OrderDto createOrderDto() {
        OrderDto orderDto = ObjectGenerator.getRandomObject(OrderDto.class);
        orderDto.setPaymentMethod(PaymentMethod.BY_CASH_TO_THE_COURIER);
        orderDto.setPhone("+79612357845");

        Set<OrderPartDto> orderParts = Set.of(createOrderPartDto(), createOrderPartDto());
        orderDto.setOrderParts(orderParts);

        return orderDto;
    }

    public OrderPartDto createOrderPartDto() {
        Dish dish = createAndSaveDish();

        OrderPartDto partDto = ObjectGenerator.getRandomObject(OrderPartDto.class);
        partDto.setOrderId(null);
        partDto.getDish().setId(dish.getId());
        partDto.getDish().setName(dish.getName());
        partDto.getDish().setPrice(dish.getPrice());
        partDto.setCount(6);

        return partDto;
    }

    public Dish createAndSaveDish() {
        Category category = ObjectGenerator.getRandomObject(Category.class);
        category.setDishes(Collections.emptyList());
        category = categoryRepository.save(category);

        Dish dish = ObjectGenerator.getRandomObject(Dish.class);
        dish.setCategory(category);
        dish.setPrice((short) 312);

        return dishRepository.save(dish);
    }
}
