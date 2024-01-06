package com.music.userMS.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

	@Bean
	WebClient.Builder getWebClientBuilder() {
		return WebClient.builder();
	}
}
