package org.scoalaonline.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class JavaApiApplication {

  @Value("${spring.application.name}")
  String applicationName;

  @GetMapping("/")
  public String home() {
    return "Welcome to " + applicationName;
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

	public static void main(String[] args) {
		SpringApplication.run(JavaApiApplication.class, args);
	}

}
