package com.restaurantonline.menuservice.utils;

import org.apache.commons.beanutils.BeanUtilsBean;

import java.lang.reflect.InvocationTargetException;

public class NullAwareBeanUtilsBean extends BeanUtilsBean {
  @Override
  public void copyProperty(Object dest, String name, Object value)
      throws IllegalAccessException, InvocationTargetException {
    if (value == null)
      return;
    super.copyProperty(dest, name, value);
  }

  public static void copyNonNullProperties(Object source, Object destination) throws Exception {
    var nullAwareBean = new NullAwareBeanUtilsBean();
    nullAwareBean.copyProperties(source, destination);
  }
}
