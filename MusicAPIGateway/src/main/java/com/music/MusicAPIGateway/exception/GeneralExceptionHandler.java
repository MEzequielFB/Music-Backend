package com.music.MusicAPIGateway.exception;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GeneralExceptionHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<HashMap<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		HashMap<String, String> errors = new HashMap<>();
		e.getBindingResult().getFieldErrors().forEach(error -> {
			errors.put(error.getField(), error.getDefaultMessage());
		});
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InvalidTokenException.class)
	public ResponseEntity<String> handleInvalidTokenException(InvalidTokenException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(MissingAuthorizationHeaderException.class)
	public ResponseEntity<String> handleMissingAuthorizationHeaderException(MissingAuthorizationHeaderException e) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
	}
}
