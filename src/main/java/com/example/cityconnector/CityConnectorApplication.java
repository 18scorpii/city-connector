package com.example.cityconnector;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class CityConnectorApplication {

	public static void main(String[] args) {
		log.info("Starting city connector application...");
		SpringApplication.run(CityConnectorApplication.class, args);
	}
}
