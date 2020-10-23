package com.ro.auth.dto.mappers;

import com.ro.auth.dto.objects.UserDto;
import com.ro.auth.model.User;
import com.ro.core.model.TelephoneNumber;
import com.ro.core.utils.TelephoneNumberUtils;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface UserDtoMapper {
  UserDtoMapper INSTANCE = Mappers.getMapper(UserDtoMapper.class);

  @Mapping(target = "phone", source = "telephoneNumber")
  UserDto toDto(User user);

  @Mapping(target = "authorities", ignore = true)
  @Mapping(target = "telephoneNumber", source = "phone")
  User toEntity(UserDto userDto);

  @Mapping(target = "bonuses", ignore = true)
  @Mapping(target = "password", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(target = "authorities", ignore = true)
  void mergeWithDto(@MappingTarget User user, UserDto userDto);

  default Set<String> toDtoAuthorities(Collection<? extends GrantedAuthority> authorities) {
    return authorities.stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toSet());
  }

  default TelephoneNumber toEntityPhone(String rawTelephoneNumber) {
    if (rawTelephoneNumber == null) {
      return null;
    }
    return TelephoneNumberUtils.fromString(rawTelephoneNumber);
  }

  default String toDtoPhone(TelephoneNumber telephoneNumber) {
    if (telephoneNumber == null) {
      return null;
    }
    return TelephoneNumberUtils.toString(telephoneNumber);
  }

//  @Named("telephoneNumberDto")
//  static String telephoneNumberToDtoTelephoneNumber(TelephoneNumber telephoneNumber) {
//    return TelephoneNumberUtils.toString(telephoneNumber);
//  }
//
//  @Named("telephoneNumberEntity")
//  static TelephoneNumber rawTelephoneNumberToEntityTelephoneNumber(String rawTelephoneNumber) {
//  }

}
