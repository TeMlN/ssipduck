package com.server.ssipduck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SsipduckApplication {

	public static void main(String[] args) {
		SpringApplication.run(SsipduckApplication.class, args);
	}

}
