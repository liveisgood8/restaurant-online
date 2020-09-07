package com.ro.menu.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = "com.ro.menu")
@EnableJpaRepositories(basePackages = "com.ro.menu.repository")
@EntityScan(basePackages = "com.ro.menu.models")
public class MenuModuleConfig {
}
