package com.example.surveysystem;

import com.example.surveysystem.models.Man;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
@EnableJpaRepositories
@SpringBootApplication
public class DemoApplication {
	public static ApplicationContext context;
	public static Man currentUser = null;
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}

