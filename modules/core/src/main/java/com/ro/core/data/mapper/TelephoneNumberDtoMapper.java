package com.ro.core.data.mapper;

import com.ro.core.data.model.TelephoneNumber;
import com.ro.core.data.repository.TelephoneNumberRepository;
import com.ro.core.utils.TelephoneNumberUtils;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class TelephoneNumberDtoMapper {
    private TelephoneNumberRepository telephoneNumberRepository;

    @Autowired
    public void setTelephoneNumberRepository(TelephoneNumberRepository telephoneNumberRepository) {
        this.telephoneNumberRepository = telephoneNumberRepository;
    }

    public String toString(TelephoneNumber telephoneNumber) {
        return TelephoneNumberUtils.toString(telephoneNumber);
    }

    public TelephoneNumber toEntity(String rawTelephoneNumber) {
        TelephoneNumber telephoneNumber = TelephoneNumberUtils.fromString(rawTelephoneNumber);
        return telephoneNumberRepository.findByCountryCodeAndNationalNumber(telephoneNumber.getCountryCode(),
                telephoneNumber.getNationalNumber())
                .orElse(telephoneNumber);
    }
}
