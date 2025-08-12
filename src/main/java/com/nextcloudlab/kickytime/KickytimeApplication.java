package com.nextcloudlab.kickytime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class KickytimeApplication {

    public static void main(String[] args) {
        SpringApplication.run(KickytimeApplication.class, args);
    }
}
