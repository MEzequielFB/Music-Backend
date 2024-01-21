package com.music.MusicAPIGateway.filter;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class RouteValidator {

	// Bypass endpoints
	public static final List<String> openApiEndpoints = List.of(
		"/api/auth/register",
		"/api/auth/login",
		"/api/auth/validate",
		"/api/user/email/**",
		"/api/user",
		"/api/user/artist"
	);
	
	public Predicate<ServerHttpRequest> isSecured =
			request -> openApiEndpoints
				.stream()
				.noneMatch( uri -> request.getURI().getPath().contains(uri) );
}
