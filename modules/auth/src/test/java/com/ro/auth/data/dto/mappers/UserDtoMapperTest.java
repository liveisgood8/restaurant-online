package com.ro.auth.data.dto.mappers;

import com.ro.auth.data.dto.objects.UserDto;
import com.ro.auth.data.model.User;
import com.ro.core.CoreTestUtils;
import com.ro.core.data.mapper.ReferenceDtoMapper;
import com.ro.core.data.mapper.TelephoneNumberDtoMapper;
import com.ro.core.data.model.TelephoneNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class UserDtoMapperTest {

  @Mock
  private TelephoneNumberDtoMapper telephoneNumberDtoMapper;

  @Mock
  private ReferenceDtoMapper referenceDtoMapper;

  @Mock
  private PasswordEncoder passwordEncoder;

  private UserDtoMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new UserDtoMapperImpl(telephoneNumberDtoMapper, referenceDtoMapper);
    mapper.setPasswordEncoder(passwordEncoder);
  }

  @Test
  void toDto() {
    User user = CoreTestUtils.getRandomObject(User.class);

    Mockito.when(telephoneNumberDtoMapper.toString(user.getTelephoneNumber())).thenReturn("+79134487325");

    UserDto userDto = mapper.toDto(user);

    assertEquals(user.getId(), userDto.getId());
    assertEquals(user.getName(), userDto.getName());
    assertEquals(user.getEmail(), userDto.getEmail());
    assertNull(userDto.getPassword());
    assertEquals("+79134487325", userDto.getPhone());
    assertEquals(user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()),
        userDto.getAuthorities());
  }

  @Test
  void toEntity() {
    UserDto userDto = CoreTestUtils.getRandomObject(UserDto.class);
    TelephoneNumber telephoneNumber = CoreTestUtils.getRandomObject(TelephoneNumber.class);

    Mockito.when(referenceDtoMapper.resolve(userDto, User.class)).thenReturn(new User());
    Mockito.when(telephoneNumberDtoMapper.toEntity(userDto.getPhone())).thenReturn(telephoneNumber);
    Mockito.when(passwordEncoder.encode(userDto.getPassword())).thenReturn("expected_password");

    User user = mapper.toEntity(userDto);
    assertUsersEqual(userDto, "expected_password", telephoneNumber, user);
  }

  @Test
  void toEntity_whenResolveExistenceUser() {
    User existUser = CoreTestUtils.getRandomObject(User.class);
    UserDto userDto = CoreTestUtils.getRandomObject(UserDto.class);
    userDto.setId(existUser.getId());

    TelephoneNumber telephoneNumber = CoreTestUtils.getRandomObject(TelephoneNumber.class);

    Mockito.when(referenceDtoMapper.resolve(userDto, User.class)).thenReturn(existUser);
    Mockito.when(telephoneNumberDtoMapper.toEntity(userDto.getPhone())).thenReturn(telephoneNumber);
    Mockito.when(passwordEncoder.encode(userDto.getPassword())).thenReturn("expected_password_new");

    User user = mapper.toEntity(userDto);
    assertUsersEqual(userDto, "expected_password_new", telephoneNumber, user);
  }

  void assertUsersEqual(UserDto expectedUserDto,
                        String expectedPassword,
                        TelephoneNumber expectedTelephoneNumber,
                        User actualUser) {
    assertEquals(expectedUserDto.getId(), actualUser.getId());
    assertEquals(expectedUserDto.getName(), actualUser.getName());
    assertEquals(expectedUserDto.getEmail(), actualUser.getEmail());
    assertEquals(expectedPassword, actualUser.getPassword());
    assertEquals(expectedTelephoneNumber, actualUser.getTelephoneNumber());
    assertEquals(expectedUserDto.getAuthorities(),
        actualUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()));
  }

}