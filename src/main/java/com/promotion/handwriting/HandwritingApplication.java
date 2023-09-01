package com.promotion.handwriting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HandwritingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HandwritingApplication.class, args);
	}

}
