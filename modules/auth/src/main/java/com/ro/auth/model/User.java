package com.ro.auth.model;

import com.ro.auth.oauth2.AuthProvider;
import com.ro.core.models.TelephoneNumber;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.util.*;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User implements OAuth2User, UserDetails {
  @Id
  @GeneratedValue
  private Long id;

  @Email
  @Column(unique = true, nullable = false, length = 64)
  private String email;

  @Column(name = "password", length = 128)
  private String password;

  @Column(name = "name", length = 32)
  private String name;

  @Column(columnDefinition = "integer default 0", nullable = false)
  private Integer bonuses;

  @Column(columnDefinition = "boolean default false", nullable = false)
  private Boolean isCredentialsExpired = false;

  @Column(columnDefinition = "boolean default false", nullable = false)
  private Boolean isBanned = false;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "telephone_number_id")
  private TelephoneNumber telephoneNumber;

  @Column(nullable = false, columnDefinition = "varchar(32) default 'NATIVE'")
  @Enumerated(EnumType.STRING)
  private AuthProvider authProvider;

  @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
  private Set<UserAuthority> authorities = Collections.emptySet();

  @Transient
  private final Map<String, Object> attributes = new HashMap<>();

  @Override
  public Map<String, Object> getAttributes() {
    return attributes;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
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
    return true;
  }
}
