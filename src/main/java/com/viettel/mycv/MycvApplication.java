package com.viettel.mycv;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MycvApplication {

	public static void main(String[] args) {
		SpringApplication.run(MycvApplication.class, args);
	}
}
