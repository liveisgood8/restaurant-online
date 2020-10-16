package com.ro.orders.model;

import com.ro.auth.model.User;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "bonus_transactions")
public class BonusesTransaction {
    @Id
    @GeneratedValue
    public Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public User user;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
}
