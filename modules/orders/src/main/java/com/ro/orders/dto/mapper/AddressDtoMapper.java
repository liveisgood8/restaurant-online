package com.ro.orders.dto.mapper;

import com.ro.core.model.Address;
import com.ro.orders.dto.objects.AddressDto;
import org.mapstruct.Mapper;

@Mapper
public interface AddressDtoMapper {
  AddressDto toDto(Address address);
  Address toEntity(AddressDto addressDto);
}
