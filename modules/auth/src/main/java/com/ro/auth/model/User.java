package com.ro.auth.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Collection;

@Entity
@Table(name = "users")
public class User implements UserDetails {
  @Id
  @GeneratedValue
  private Long id;

  @Email
  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column
  private String name;

  @Column
  private String surname;

  @Column(columnDefinition = "boolean default true", nullable = false)
  private Boolean isEnabled = true;

  @Column(columnDefinition = "boolean default false", nullable = false)
  private Boolean isExpired = false;

  @Column(columnDefinition = "boolean default false", nullable = false)
  private Boolean isCredentialsExpired = false;

  @Column(columnDefinition = "boolean default false", nullable = false)
  private Boolean isBanned = false;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return null;
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

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }
}
