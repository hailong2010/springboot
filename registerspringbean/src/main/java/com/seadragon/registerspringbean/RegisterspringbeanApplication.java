package com.seadragon.registerspringbean;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import com.seadragon.registerspringbean.annotation.EnableJmsEvent;
import com.seadragon.registerspringbean.delegate.OrderEvent;

@SpringBootApplication
@EnableJmsEvent(scanPath = OrderEvent.class)
public class RegisterspringbeanApplication {

	public static void main(String[] args) {
		SpringApplication.run(RegisterspringbeanApplication.class, args);
	}

	@Bean()
	public CommandLineRunner hailong(ApplicationContext appContext) {
		return args -> {
			OrderEvent bean = (OrderEvent) appContext
					.getBean(OrderEvent.class.getName());
			bean.createOrder();
			bean.cancelOrder();
		};
	}
}
