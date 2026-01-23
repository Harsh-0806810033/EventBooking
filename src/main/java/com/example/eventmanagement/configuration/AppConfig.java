package com.example.eventmanagement.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.eventmanagement.entities.User;
import com.example.eventmanagement.repository.UserRepository;

@Configuration
public class AppConfig {

	@Autowired
	private UserRepository repo;
	
	@Bean
	CommandLineRunner runner(UserRepository repo, PasswordEncoder encoder) {
	    return args -> {
	        if (repo.findByEmail("admin@test.com").isEmpty()) {
	            User u = new User();
	            u.setEmail("admin@test.com");
	            u.setPassword(encoder.encode("password"));
	            repo.save(u);
	        }
	    };
	}
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
