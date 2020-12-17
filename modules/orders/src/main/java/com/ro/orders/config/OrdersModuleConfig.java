package com.ro.orders.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = "com.ro.orders")
@EnableJpaRepositories(basePackages = "com.ro.orders.data.repository")
@EntityScan(basePackages = "com.ro.orders.data.model")
public class OrdersModuleConfig {
}