package com.ro.app;

import com.ro.auth.config.AuthModuleConfig;
import com.ro.core.CoreModuleConfig;
import com.ro.menu.config.MenuModuleConfig;
import com.ro.orders.config.OrdersModuleConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({
		CoreModuleConfig.class,
		AuthModuleConfig.class,
		MenuModuleConfig.class,
		OrdersModuleConfig.class,
})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
