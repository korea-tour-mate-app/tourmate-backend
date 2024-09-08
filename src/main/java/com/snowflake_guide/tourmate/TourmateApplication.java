package com.snowflake_guide.tourmate;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients(basePackages = "com.snowflake_guide.tourmate.global.client")
public class TourmateApplication {

	public static void main(String[] args) {
		SpringApplication.run(TourmateApplication.class, args);
	}

}
