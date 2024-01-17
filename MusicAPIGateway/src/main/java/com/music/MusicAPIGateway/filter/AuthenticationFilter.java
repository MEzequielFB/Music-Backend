package com.music.MusicAPIGateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.music.MusicAPIGateway.util.JwtUtil;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

	@Autowired
	private RouteValidator routeValidator;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	public AuthenticationFilter() {
		super(Config.class);
	}
	
	@Override
	public GatewayFilter apply(Config config) {
		return ((exchange, chain) -> {
			
			if (routeValidator.isSecured.test(exchange.getRequest())) {
				// header contains token or not
				if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
					throw new RuntimeException("missing authorization header");
				}
				
				String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
				if (authHeader != null && authHeader.startsWith("Bearer ")) {
					authHeader = authHeader.substring(7); // get the token
				}
				
				try {
					jwtUtil.validateToken(authHeader);
				} catch (Exception e) {
					throw new RuntimeException("invalid token");
				}
			}
			return chain.filter(exchange);
		});
	}
	
	public static class Config {
		
	}
}
