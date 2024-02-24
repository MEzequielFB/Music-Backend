package com.music.accountTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.music.userMS.controller.AccountController;
import com.music.userMS.dto.AccountRequestDTO;
import com.music.userMS.dto.AccountResponseDTO;
import com.music.userMS.dto.BalanceDTO;
import com.music.userMS.dto.UserIdDTO;
import com.music.userMS.dto.UserResponseDTO;
import com.music.userMS.exception.AddUserException;
import com.music.userMS.exception.AlreadyContainsException;
import com.music.userMS.exception.AuthorizationException;
import com.music.userMS.exception.MultipleUsersLinkedToAccountException;
import com.music.userMS.exception.NotEnoughBalanceException;
import com.music.userMS.exception.NotFoundException;
import com.music.userMS.exception.SomeEntityDoesNotExistException;
import com.music.userMS.service.AccountService;

public class AccountControllerTest {

	@Mock
	private AccountService service;
	
	@InjectMocks
	private AccountController controller;
	
	private AccountResponseDTO accountResponseMock;
	private AccountRequestDTO accountRequestMock;
	private UserIdDTO userRequestMock;
	private BalanceDTO balanceRequestMock;
	private String tokenMock;
	
	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
		this.accountResponseMock = AccountResponseDTO.builder()
				.id(1)
				.users(List.of(UserResponseDTO.builder()
						.id(1)
						.username("eze")
						.email("eze@gmail.com")
						.role("USER")
						.build()))
				.balance(20.0)
				.build();
		this.accountRequestMock = AccountRequestDTO.builder()
				.balance(20.0)
				.build();
		this.userRequestMock = UserIdDTO.builder()
				.userId(2)
				.build();
		this.balanceRequestMock = BalanceDTO.builder()
				.balance(40.0)
				.build();
		this.tokenMock = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdXBlcmFkbWluQGdtYWlsLmNvbSIsImlkIjoyMywiYXV0aCI6IlNVUEVSX0FETUlOIiwiZXhwIjoxNzA3NTMyNDU1fQ.JWKKgNu9xZs6c0lr7ZeRd2v_FFCyTle7XpbumUMeQXPLZYJ1TfTydpESzImAnbljMgZ-OrKCVTHytbVyl4edbQ";
	}
	
	@Test
	public void findAllTest() {
		List<AccountResponseDTO> accountsResponseMock = List.of(accountResponseMock);
		when(service.findAll()).thenReturn(accountsResponseMock);
		
		ResponseEntity<List<AccountResponseDTO>> responseEntity = controller.findAll();
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(accountsResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void findByIdTest() throws NotFoundException {
		when(service.findById(1)).thenReturn(accountResponseMock);
		
		ResponseEntity<AccountResponseDTO> responseEntity = controller.findById(1);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(accountResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void saveAccountTest() throws SomeEntityDoesNotExistException, AuthorizationException, NotFoundException, AddUserException {
		when(service.saveAccount(accountRequestMock, tokenMock)).thenReturn(accountResponseMock);
		
		ResponseEntity<AccountResponseDTO> responseEntity = controller.saveAccount(accountRequestMock, tokenMock);
		
		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertEquals(accountResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void addUserTest() throws NotFoundException, AlreadyContainsException, AuthorizationException, AddUserException {
		when(service.addUser(1, 2, tokenMock)).thenReturn(accountResponseMock);
		
		ResponseEntity<AccountResponseDTO> responseEntity = controller.addUser(1, userRequestMock, tokenMock);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(accountResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void removeUserTest() throws NotFoundException, AuthorizationException {
		when(service.removeUser(1, 2, tokenMock)).thenReturn(accountResponseMock);
		
		ResponseEntity<AccountResponseDTO> responseEntity = controller.removeUser(1, userRequestMock, tokenMock);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(accountResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void addBalanceTest() throws NotFoundException, AuthorizationException {
		when(service.addBalance(1, balanceRequestMock, tokenMock)).thenReturn(accountResponseMock);
		
		ResponseEntity<AccountResponseDTO> responseEntity = controller.addBalance(1, balanceRequestMock, tokenMock);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(accountResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void removeBalanceTest() throws NotFoundException, NotEnoughBalanceException, AuthorizationException {
		when(service.removeBalance(1, balanceRequestMock, tokenMock)).thenReturn(accountResponseMock);
		
		ResponseEntity<AccountResponseDTO> responseEntity = controller.removeBalance(1, balanceRequestMock, tokenMock);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(accountResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void deleteAccountTest() throws NotFoundException, MultipleUsersLinkedToAccountException, AuthorizationException {
		when(service.deleteAccount(1, tokenMock)).thenReturn(accountResponseMock);
		 
		ResponseEntity<AccountResponseDTO> responseEntity = controller.deleteAccount(1, tokenMock);
		 
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(accountResponseMock, responseEntity.getBody());
	}
}
