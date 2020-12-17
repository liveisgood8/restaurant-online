package com.ro.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Import(SecurityConfigAccumulator.class)
public class UnitTestSecurityConfig extends WebSecurityConfigurerAdapter {
  @Autowired
  private SecurityConfigAccumulator securityConfigAccumulator;

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    securityConfigAccumulator.apply(httpSecurity);
  }
}
