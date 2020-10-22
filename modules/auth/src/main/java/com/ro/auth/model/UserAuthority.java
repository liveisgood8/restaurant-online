package com.ro.auth.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user_authorities")
public class UserAuthority implements GrantedAuthority {
  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "authority", nullable = false, length = 64)
  private String authority;

  @Override
  public String getAuthority() {
    return authority;
  }
}
