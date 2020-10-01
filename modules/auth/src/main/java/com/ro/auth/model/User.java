package com.ro.auth.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements UserDetails {
  @Id
  @GeneratedValue
  private Long id;

  @Pattern(regexp="^((\\+7|7|8)+([0-9]){10})$")
  @Column(unique = true, nullable = false)
  private String phone;

  @Email
  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column
  private String name;

  @Column(columnDefinition = "integer default 0", nullable = false)
  private Integer bonuses;

  @Column(columnDefinition = "boolean default true", nullable = false)
  private Boolean isEnabled = true;

  @Column(columnDefinition = "boolean default false", nullable = false)
  private Boolean isExpired = false;

  @Column(columnDefinition = "boolean default false", nullable = false)
  private Boolean isCredentialsExpired = false;

  @Column(columnDefinition = "boolean default false", nullable = false)
  private Boolean isBanned = false;

  @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
  private Set<UserAuthority> authorities = Collections.emptySet();

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public boolean isAccountNonExpired() {
    return !isExpired;
  }

  @Override
  public boolean isAccountNonLocked() {
    return !isBanned;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return !isCredentialsExpired;
  }

  @Override
  public boolean isEnabled() {
    return isEnabled;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getBonuses() {
    return bonuses;
  }

  public void setBonuses(Integer bonuses) {
    this.bonuses = bonuses;
  }
}
