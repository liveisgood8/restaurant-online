package com.ro.app;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
      registry.addViewController("/{x:[\\w\\-]+}")
        .setViewName("forward:/");
    registry.addViewController("/{x:^(?!api$).*$}/**/{y:[\\w\\-]+}")
        .setViewName("forward:/");
    registry.addViewController("/{spring:\\w+}/**{spring:?!(\\.js|\\.css)$}")
        .setViewName("forward:/");
  }
}