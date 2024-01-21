package com.music.MusicAPIGateway.util;

import java.security.Key;

import org.springframework.stereotype.Service;

import com.music.MusicAPIGateway.exception.InvalidTokenException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service("jwtService")
public class JwtUtil {

	public static final String SECRET = "QJeKx+s7XIv1WbBlj7vJ9CD3Ozj1rB3qjlNZY9ofWKJSaBNBo5r1q9Rru/OWlYb+UHV1n4/LJl1OBYYZZ7rhJEnn5peyHCd+eLJfRdArE37pc+QDIsJlabQtR7tYRa+SnvGRyL01uZsK33+gezV+/GPXBnPTj8fOojDUzJiPAvE=";
	
	public void validateToken(final String token) throws InvalidTokenException {
		try {
			Jwts.parserBuilder()
				.setSigningKey(getSignKey())
				.build()
				.parseClaimsJws(token);
		} catch (Exception e) {
			throw new InvalidTokenException();
		}
	}
	
	private Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
