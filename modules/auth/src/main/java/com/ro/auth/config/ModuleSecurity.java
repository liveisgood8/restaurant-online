package com.ro.auth.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

public interface ModuleSecurity {
  void configure(HttpSecurity httpSecurity) throws Exception;
}
