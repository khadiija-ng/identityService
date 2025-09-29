package com.disi.identyService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class IdentyServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IdentyServiceApplication.class, args);
	}

}
