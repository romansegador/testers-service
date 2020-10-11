package com.rsegador.workshop.testersservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class TestersServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestersServiceApplication.class, args);
	}

}
