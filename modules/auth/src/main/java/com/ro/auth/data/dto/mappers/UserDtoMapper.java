package com.ro.auth.data.dto.mappers;

import com.ro.auth.data.dto.objects.UserDto;
import com.ro.auth.data.model.User;
import com.ro.core.data.mapper.ReferenceDtoMapper;
import com.ro.core.data.mapper.TelephoneNumberDtoMapper;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    componentModel = "spring",
    uses = {TelephoneNumberDtoMapper.class, ReferenceDtoMapper.class})
public abstract class UserDtoMapper {
  private PasswordEncoder passwordEncoder;

  @Autowired
  public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @Mapping(target = "phone", source = "telephoneNumber")
  @Mapping(target = "password", ignore = true)
  public abstract UserDto toDto(User user);

  @Mapping(target = "telephoneNumber", source = "phone")
  @Mapping(target = "authorities", ignore = true)
  @Mapping(target = "bonuses", ignore = true)
  @Mapping(target = "password", ignore = true)
  public abstract User toEntity(UserDto userDto);

  protected Set<String> toDtoAuthorities(Collection<? extends GrantedAuthority> authorities) {
    return authorities.stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toSet());
  }

  @Named("encodePassword")
  protected String encodeDtoPassword(User user, String password) {
    if (password == null) {
      return user.getPassword();
    }
    return passwordEncoder.encode(password);
  }

  @AfterMapping
  public void afterMappingToEntity(UserDto dto, @MappingTarget User entity) {
    if (dto.getPassword() != null) {
      entity.setPassword(passwordEncoder.encode(dto.getPassword()));
    }
  }
}
