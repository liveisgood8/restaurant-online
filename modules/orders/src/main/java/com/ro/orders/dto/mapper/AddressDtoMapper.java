package com.ro.orders.dto.mapper;

import com.ro.core.model.Address;
import com.ro.orders.dto.objects.AddressDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressDtoMapper {
  AddressDto toDto(Address address);
  Address toEntity(AddressDto addressDto);
}
