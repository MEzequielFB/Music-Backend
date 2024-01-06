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
import com.music.userMS.exception.AlreadyContainsException;
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
				.usersId(List.of(1))
				.balance(20.0)
				.build();
		this.userRequestMock = UserIdDTO.builder()
				.userId(2)
				.build();
		this.balanceRequestMock = BalanceDTO.builder()
				.balance(40.0)
				.build();
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
	public void saveAccountTest() throws SomeEntityDoesNotExistException {
		when(service.saveAccount(accountRequestMock)).thenReturn(accountResponseMock);
		
		ResponseEntity<AccountResponseDTO> responseEntity = controller.saveAccount(accountRequestMock);
		
		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertEquals(accountResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void addUserTest() throws NotFoundException, AlreadyContainsException {
		when(service.addUser(1, 2)).thenReturn(accountResponseMock);
		
		ResponseEntity<AccountResponseDTO> responseEntity = controller.addUser(1, userRequestMock);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(accountResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void removeUserTest() throws NotFoundException {
		when(service.removeUser(1, 2)).thenReturn(accountResponseMock);
		
		ResponseEntity<AccountResponseDTO> responseEntity = controller.removeUser(1, userRequestMock);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(accountResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void addBalanceTest() throws NotFoundException {
		when(service.addBalance(1, balanceRequestMock)).thenReturn(accountResponseMock);
		
		ResponseEntity<AccountResponseDTO> responseEntity = controller.addBalance(1, balanceRequestMock);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(accountResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void removeBalanceTest() throws NotFoundException, NotEnoughBalanceException {
		when(service.removeBalance(1, balanceRequestMock)).thenReturn(accountResponseMock);
		
		ResponseEntity<AccountResponseDTO> responseEntity = controller.removeBalance(1, balanceRequestMock);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(accountResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void deleteAccountTest() throws NotFoundException, MultipleUsersLinkedToAccountException {
		when(service.deleteAccount(1)).thenReturn(accountResponseMock);
		 
		ResponseEntity<AccountResponseDTO> responseEntity = controller.deleteAccount(1);
		 
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(accountResponseMock, responseEntity.getBody());
	}
}
