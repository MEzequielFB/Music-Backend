package com.music.authenticationMS.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.music.authenticationMS.security.JWTFilter;
import com.music.authenticationMS.security.TokenProvider;

@Configuration
@EnableWebSecurity
public class AuthConfig {
	
	@Autowired
    private TokenProvider tokenProvider;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		return http.csrf().disable()
//				.authorizeHttpRequests()
//				.requestMatchers("api/auth/register", "api/auth/login", "api/auth/validate").permitAll()
//				.and()
//				.build();
//		return http
//                .authorizeExchange(authorizeExchange ->
//                        authorizeExchange
//                                .pathMatchers("/api/auth/register", "/api/auth/login", "/api/auth/validate").permitAll()
//                                .anyExchange().authenticated()
//                )
//                .csrf().disable()
//                .build();
		
//		http
//        	.apply(securityConfigurerAdapter());
		http
			.addFilterBefore(new JWTFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);
		http
		    .csrf( AbstractHttpConfigurer::disable )
		    .authorizeHttpRequests( auth -> auth
		    		.anyRequest().permitAll()
		    		//.anyRequest().authenticated()
		    )
		    .anonymous( AbstractHttpConfigurer::disable )
		    .sessionManagement( s -> s.sessionCreationPolicy( SessionCreationPolicy.STATELESS ) );
		http
		    .httpBasic( Customizer.withDefaults() );
		
		return http.build();
	}
	
//	private JwtConfigurer securityConfigurerAdapter() {
//        return new JwtConfigurer(tokenProvider);
//    }
	
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
//	
//	@Bean
//	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//		return config.getAuthenticationManager();
//	}
//	
//	@Bean
//	public UserDetailsService userDetailsService() {
//		return new CustomUserDetailsService();
//	}
//	
//	@Bean
//	public AuthenticationProvider authenticationProvider() {
//		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//		authenticationProvider.setUserDetailsService(userDetailsService());
//		authenticationProvider.setPasswordEncoder(passwordEncoder());
//		return authenticationProvider;
//	}
}
