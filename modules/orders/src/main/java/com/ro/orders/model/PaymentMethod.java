package com.ro.orders.model;

import com.ro.core.model.BaseEnumEntity;

import javax.persistence.*;

@Entity
@Table(name = "payment_methods")
public class PaymentMethod extends BaseEnumEntity {
    public static final String BY_CASH_TO_THE_COURIER = "BY_CASH_TO_THE_COURIER";
    public static final String BY_CARD_TO_THE_COURIER = "BY_CARD_TO_THE_COURIER";
    public static final String BY_CARD_ONLINE = "BY_CARD_ONLINE";
}
