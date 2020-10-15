package com.ro.core.utils;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.ro.core.models.TelephoneNumber;

import java.util.Locale;

public class TelephoneNumberUtils {
  private final static PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
  private final static String locale = Locale.getDefault().getCountry();

  public static boolean isMobileTelephoneNumber(String data) {
    if (data == null || data.isBlank()) {
      return false;
    }

    try {
      Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(data, locale);
      PhoneNumberUtil.PhoneNumberType phoneNumberType = phoneNumberUtil.getNumberType(phoneNumber);
      return phoneNumberType == PhoneNumberUtil.PhoneNumberType.MOBILE;
    } catch (Exception ignore) {}
    return false;
  }

  public static String toString(TelephoneNumber telephoneNumber) {
    return "+" + telephoneNumber.getCountryCode() + telephoneNumber.getNationalNumber();
  }

  /**
   * Парсит только номера с российским кодом региона
   * @param telephoneNumberString Строковое представление номера телефона
   * @return Объект представляющий номер
   * @throws RuntimeException Ошибка парсинга номера телефона или невалидный формат
   */
  public static TelephoneNumber fromString(String telephoneNumberString) {
    try {
      Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(telephoneNumberString, locale);

      TelephoneNumber telephoneNumber = new TelephoneNumber();
      telephoneNumber.setCountryCode(String.valueOf(phoneNumber.getCountryCode()));
      telephoneNumber.setNationalNumber(nationalNumberAsString(phoneNumber.getNationalNumber(),
          phoneNumber.getNumberOfLeadingZeros()));

      return telephoneNumber;
    } catch (NumberParseException ex) {
      throw new RuntimeException(ex.getMessage());
    }
  }

  private static String nationalNumberAsString(long number, int leadingZeros) {
    if (leadingZeros == 0) {
      return String.valueOf(number);
    }
    return String.format("%0" + leadingZeros + "d", number);
  }
}
