package com.decodedbytes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.decodedbytes.beans")
public class SpringCamelProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCamelProjectApplication.class, args);
	}

}

