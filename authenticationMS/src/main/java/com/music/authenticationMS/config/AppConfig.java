package com.music.authenticationMS.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import com.music.authenticationMS.exception.ClientException;

import reactor.core.publisher.Mono;

@Configuration
public class AppConfig {
	
	public static ExchangeFilterFunction errorHandler() {
	    return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
	        if (clientResponse.statusCode().is4xxClientError()) {
	            return clientResponse.bodyToMono(String.class)
	                    .flatMap(errorBody -> Mono.error(new ClientException(errorBody)));
	        } else {
	            return Mono.just(clientResponse);
	        }
	    });
	}

	@Bean
	WebClient getWebClient() {
		return WebClient
				.builder()
				.filter(errorHandler())
				.build();
	}
}
