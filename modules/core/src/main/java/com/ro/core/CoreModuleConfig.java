package com.ro.core;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan("com.ro.core")
@EnableJpaRepositories(basePackages = "com.ro.core.data.repository")
@EntityScan(basePackages = "com.ro.core.data.model")
public class CoreModuleConfig {
}
