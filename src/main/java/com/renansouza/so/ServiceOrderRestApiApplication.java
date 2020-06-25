package com.renansouza.so;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.renansouza.so.models.*")
public class ServiceOrderRestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceOrderRestApiApplication.class, args);
	}

}
