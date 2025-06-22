package com.example.vet_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class VetBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(VetBackendApplication.class, args);
	}
//gggggggggggggggggggg
}
