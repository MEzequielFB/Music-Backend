package com.music.authenticationMS.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.music.authenticationMS.exception.InvalidTokenException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service("jwtService")
public class JwtService {

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
	
	public String generateToken(String userName) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userName);
	}
	
	private String createToken(Map<String, Object> claims, String userName) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(userName)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
				.signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
	}
	
	private Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
