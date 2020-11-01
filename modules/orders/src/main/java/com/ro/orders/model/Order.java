package com.ro.orders.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.ro.auth.model.User;
import com.ro.core.model.Address;
import com.ro.core.model.TelephoneNumber;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Order.class)
@Entity
@Table(name = "orders")
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "is_approved", nullable = false)
  private Boolean isApproved;

  @OneToOne
  @JoinColumn(name = "payment_method_id", nullable = false)
  private PaymentMethod paymentMethod;

  @OneToOne
  @JoinColumn(name = "address_id", nullable = false)
  private Address address;

  @OneToOne
  @JoinColumn(name = "telephone_number_id", nullable = false)
  private TelephoneNumber telephoneNumber;

  @OneToMany(mappedBy = "id.orderId", orphanRemoval = true, cascade = CascadeType.ALL)
  private Set<OrderPart> orderParts = new HashSet<>();

  @OneToMany(mappedBy = "order", orphanRemoval = true)
  private Set<BonusesTransaction> bonusesTransactions = new HashSet<>();

  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "created_at")
  private Date createdAt;

  public int getTotalPrice() {
    int totalPartPrice = orderParts.stream()
            .mapToInt(OrderPart::getTotalPrice)
            .sum();

    return totalPartPrice - getSpentBonuses();
  }

  public int getReceivedBonuses() {
    return bonusesTransactions.stream()
            .mapToInt(BonusesTransaction::getAmount)
            .filter(a -> a > 0)
            .sum();
  }

  public int getSpentBonuses() {
    return bonusesTransactions.stream()
            .mapToInt(BonusesTransaction::getAmount)
            .filter(a -> a < 0)
            .sum() * -1;
  }
}
