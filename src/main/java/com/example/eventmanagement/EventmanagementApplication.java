package com.example.eventmanagement;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class EventmanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventmanagementApplication.class, args);
	}
	
	@PostConstruct
	public void init() {
	    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

}
