package com.music.userMS.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.music.userMS.model.Roles;
import com.music.userMS.security.JwtFilter;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@EnableMethodSecurity
@EnableWebSecurity
@Configuration
public class SecurityConfig {
	
    private final JwtParser jwtParser;
    private final String secret = "QJeKx+s7XIv1WbBlj7vJ9CD3Ozj1rB3qjlNZY9ofWKJSaBNBo5r1q9Rru/OWlYb+UHV1n4/LJl1OBYYZZ7rhJEnn5peyHCd+eLJfRdArE37pc+QDIsJlabQtR7tYRa+SnvGRyL01uZsK33+gezV+/GPXBnPTj8fOojDUzJiPAvE=";

    public SecurityConfig() {
        final var keyBytes = Decoders.BASE64.decode(secret);
        final var key = Keys.hmacShaKeyFor( keyBytes );
        jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
    }
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
    		.addFilterBefore( new JwtFilter( jwtParser ), UsernamePasswordAuthenticationFilter.class);
		http
		    .csrf( AbstractHttpConfigurer::disable )
		    .authorizeHttpRequests( auth -> auth
		    		// Role
		    		.requestMatchers("/api/role/**").hasAuthority(Roles.ADMIN)
		    		//Account
		    		.requestMatchers("/api/account/**").hasAuthority(Roles.USER)
		    		.requestMatchers(HttpMethod.GET, "/api/account/**").hasAuthority(Roles.ADMIN)
		    		// User
//		    		.requestMatchers("/api/user/**").hasAnyAuthority(Roles.ADMIN, Roles.USER, Roles.ARTIST)	
//		    		.requestMatchers("/api/user/deleted").hasAuthority(Roles.ADMIN)
//		    		.requestMatchers(HttpMethod.DELETE, "/api/user/**").hasAuthority(Roles.ADMIN)
//		    		.requestMatchers(HttpMethod.POST, "/api/user/**").permitAll()
//		    		.requestMatchers("/api/user/email/**").permitAll()
//		    		.requestMatchers("/api/user/follow").hasAnyAuthority(Roles.USER, Roles.ARTIST)
		    		.requestMatchers("/api/user/**").permitAll()
		    )
		    .anonymous( AbstractHttpConfigurer::disable )
		    .sessionManagement( s -> s.sessionCreationPolicy( SessionCreationPolicy.STATELESS ) );
		http
		    .httpBasic( Customizer.withDefaults() );
		
		return http.build();
	}
}
