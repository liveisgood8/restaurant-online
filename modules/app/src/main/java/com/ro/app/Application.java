package com.ro.app;

import com.ro.menu.config.MenuModuleConfig;
import com.ro.orders.config.OrdersServiceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({
		MenuModuleConfig.class,
		OrdersServiceConfig.class,
})

public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
