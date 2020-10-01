package com.ro.core;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.ro.core.repository")
@EntityScan(basePackages = "com.ro.core.models")
public class CoreModuleConfig {
}
