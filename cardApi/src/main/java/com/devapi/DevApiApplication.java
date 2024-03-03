package com.devapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.devapi"})
@EnableJpaRepositories(basePackages = {"com.devapi"})
@EntityScan(basePackages = {"com.devapi"})
public class DevApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevApiApplication.class, args);
	}

}
