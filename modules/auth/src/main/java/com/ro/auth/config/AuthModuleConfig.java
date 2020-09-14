package com.ro.auth.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@ComponentScan(basePackages = "com.ro.auth")
@EntityScan(basePackages = "com.ro.auth.model")
@EnableJpaRepositories(basePackages = "com.ro.auth.repository")
public class AuthModuleConfig {
}
