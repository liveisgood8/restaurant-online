package com.ro.orders.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.ro.auth.model.User;
import com.ro.core.models.Address;
import com.ro.orders.validation.InsertGroup;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Order.class)
@Entity
@Table(name = "orders")
public class Order {
  public enum PaymentMethod {
    BY_CASH_TO_THE_COURIER,
    BY_CARD_TO_THE_COURIER,
    BY_CARD_ONLINE,
  }

  @Id
  @GeneratedValue
  private Long id;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Column(nullable = false)
  private Boolean isApproved;

  @Column(nullable = false)
  @Enumerated
  private PaymentMethod paymentMethod;

  @OneToOne(cascade = CascadeType.REMOVE)
  @JoinColumn(name = "address_id", nullable = false)
  private Address address;

  @OneToMany(mappedBy = "id.orderId", orphanRemoval = true)
  @NotNull(groups = {InsertGroup.class})
  private Set<OrderInfo> orderInfos;

  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "created_at")
  private Date createdAt;
}
