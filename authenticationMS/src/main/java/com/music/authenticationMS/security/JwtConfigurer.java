package com.music.authenticationMS.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configura el filtro JWT FILTER para que resuelva el token antes de cada request.
 */
public class JwtConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

//	@Autowired
//    private TokenProvider tokenProvider;
	
	private TokenProvider tokenProvider;
	
	public JwtConfigurer(TokenProvider tokenProvider) {
		this.tokenProvider = new TokenProvider();
	}

    @Override
    public void configure( HttpSecurity http ) {
        JWTFilter customFilter = new JWTFilter(tokenProvider);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
