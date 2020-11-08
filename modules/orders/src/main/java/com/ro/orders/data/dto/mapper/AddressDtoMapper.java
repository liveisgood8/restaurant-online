package com.ro.orders.data.dto.mapper;

import com.ro.core.data.model.Address;
import com.ro.orders.data.dto.objects.AddressDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressDtoMapper {
  AddressDto toDto(Address address);
  Address toEntity(AddressDto addressDto);
}
