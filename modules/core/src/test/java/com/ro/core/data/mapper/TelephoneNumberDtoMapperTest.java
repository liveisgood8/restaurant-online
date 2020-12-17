package com.ro.core.data.mapper;

import com.ro.core.data.model.TelephoneNumber;
import com.ro.core.data.repository.TelephoneNumberRepository;
import com.ro.core.utils.TelephoneNumberUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class TelephoneNumberDtoMapperTest {

    @Mock
    private TelephoneNumberRepository telephoneNumberRepository;

    private TelephoneNumberDtoMapper mapper;

    @BeforeEach
    void initialize() {
        mapper = Mappers.getMapper(TelephoneNumberDtoMapper.class);
        mapper.setTelephoneNumberRepository(telephoneNumberRepository);
    }

    @Test
    void testToString() {
        TelephoneNumber telephoneNumber = new TelephoneNumber();
        telephoneNumber.setCountryCode("7");
        telephoneNumber.setNationalNumber("9135681235");

        assertEquals("+79135681235", mapper.toString(telephoneNumber));
    }

    @Test
    void toEntity_whenNotExistInDb() {
        String countryCode = "7";
        String nationalNumber = "9135681235";
        Mockito.when(telephoneNumberRepository.findByCountryCodeAndNationalNumber(countryCode, nationalNumber))
                .thenReturn(Optional.empty());

        TelephoneNumber telephoneNumber = mapper.toEntity("+" + countryCode + nationalNumber);

        assertNull(telephoneNumber.getId());
        assertEquals(countryCode, telephoneNumber.getCountryCode());
        assertEquals(nationalNumber, telephoneNumber.getNationalNumber());
    }

    @Test
    void toEntity_whenExistInDb() {
        String countryCode = "7";
        String nationalNumber = "9135681235";

        TelephoneNumber givenTelephoneNumber = new TelephoneNumber();
        givenTelephoneNumber.setId(1L);
        givenTelephoneNumber.setCountryCode(countryCode);
        givenTelephoneNumber.setNationalNumber(nationalNumber);
        Mockito.when(telephoneNumberRepository.findByCountryCodeAndNationalNumber(countryCode, nationalNumber))
                .thenReturn(Optional.of(givenTelephoneNumber));

        TelephoneNumber telephoneNumber = mapper.toEntity("+" + countryCode + nationalNumber);

        assertEquals(givenTelephoneNumber.getId(), telephoneNumber.getId());
        assertEquals(countryCode, telephoneNumber.getCountryCode());
        assertEquals(nationalNumber, telephoneNumber.getNationalNumber());
    }
}