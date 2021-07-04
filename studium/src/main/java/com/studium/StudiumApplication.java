package com.studium;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@PropertySource("classpath:application.properties")

public class StudiumApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudiumApplication.class, args);
	}

}
