package com.ro.auth.data.model;

import lombok.Getter;

import javax.persistence.*;

@Table(name = "bonuses_balance")
@Entity
@Getter
public class UserBonusesBalance {
  @Id
  private Long userId;

  @OneToOne
  @MapsId
  private User user;

  @Column(name = "balance", nullable = false)
  private Integer amount;
}
