package com.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class MTlsClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(MTlsClientApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(RestTemplate restTemplate) {
		return args -> {
			try {
				String response = restTemplate.getForObject("https://localhost:8443/secure", String.class);
				System.out.println("Response from server: " + response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	}
}