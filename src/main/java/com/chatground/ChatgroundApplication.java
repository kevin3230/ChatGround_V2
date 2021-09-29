package com.chatground;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@SpringBootApplication
@EnableScheduling
public class ChatgroundApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatgroundApplication.class, args);
	}

}
