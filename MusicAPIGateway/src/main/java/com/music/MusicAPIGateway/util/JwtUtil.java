package com.music.MusicAPIGateway.util;

import java.security.Key;

import org.springframework.stereotype.Service;

import com.music.MusicAPIGateway.exception.InvalidTokenException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service("jwtService")
public class JwtUtil {

	public static final String SECRET = "3HF82HJ2JKLNDSMB353382HN029MCMN2FHSA98SF9A8M32H239MH2389FHM239F8HM29FH23";
	
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
