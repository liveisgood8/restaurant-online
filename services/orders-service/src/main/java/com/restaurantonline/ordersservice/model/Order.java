package com.restaurantonline.ordersservice.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.restaurantonline.ordersservice.validation.InsertGroup;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Table(name = "orders")
public class Order {
  @Id
  @GeneratedValue
  private Long id;

  @Column
  @NotNull(groups = {InsertGroup.class})
  private Long clientId;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  @NotNull(groups = {InsertGroup.class})
  private Set<OrderInfo> orderInfos;

  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "created_at")
  private Date createdAt;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getClientId() {
    return clientId;
  }

  public void setClientId(Long clientId) {
    this.clientId = clientId;
  }

  public Set<OrderInfo> getOrderInfos() {
    return orderInfos;
  }

  public void setOrderInfos(Set<OrderInfo> orderInfos) {
    this.orderInfos = orderInfos;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }
}
