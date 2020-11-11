package com.ro.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import java.util.List;

@Configuration
public class SecurityConfigAccumulator {
  private final List<ModuleSecurity> modulesSecurity;

  @Autowired
  public SecurityConfigAccumulator(List<ModuleSecurity> modulesSecurity) {
    this.modulesSecurity = modulesSecurity;
  }

  public void apply(HttpSecurity httpSecurity) throws Exception {
    for (ModuleSecurity moduleSecurity : modulesSecurity) {
      moduleSecurity.configure(httpSecurity);
    }

    httpSecurity.authorizeRequests()
        .anyRequest().authenticated();
  }
}
