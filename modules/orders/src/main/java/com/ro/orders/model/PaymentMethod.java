package com.ro.orders.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "payment_methods")
public class PaymentMethod {
    public enum Name {
        BY_CASH_TO_THE_COURIER,
        BY_CARD_TO_THE_COURIER,
        BY_CARD_ONLINE,
    }

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Short id;

    @Enumerated(value = EnumType.STRING)
    @Column(length = 32, nullable = false)
    private Name name;
}
