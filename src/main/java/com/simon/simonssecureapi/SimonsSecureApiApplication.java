package com.simon.simonssecureapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

//@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@SpringBootApplication
public class SimonsSecureApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimonsSecureApiApplication.class, args);
    }

    @Bean
    public CommandLineRunner init(@Value("${server.port:8080}") String port) {
        return args -> {
            System.out.println("Application is running on port: " + port);
        };
    }
}
