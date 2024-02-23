package com.music.userTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.music.userMS.controller.UserController;
import com.music.userMS.dto.RoleUpdateRequestDTO;
import com.music.userMS.dto.UserDetailsResponseDTO;
import com.music.userMS.dto.UserRequestDTO;
import com.music.userMS.dto.UserResponseDTO;
import com.music.userMS.exception.EmailAlreadyUsedException;
import com.music.userMS.exception.InvalidRoleException;
import com.music.userMS.exception.NotFoundException;
import com.music.userMS.service.UserService;

public class UserControllerTest {

	@Mock
	private UserService service;
	
	@InjectMocks
	private UserController controller;
	
	private UserResponseDTO userResponseMock;
	private UserResponseDTO deletedUserResponseMock;
	private UserRequestDTO userRequestMock;
	private UserDetailsResponseDTO userDetailsResponseMock;
	private RoleUpdateRequestDTO roleUpdateRequestMock;
	
	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
		this.userResponseMock = UserResponseDTO.builder()
				.id(1)
				.username("eze")
				.email("eze@gmail.com")
				.address("street 123")
				.role("USER")
				.build();
		this.deletedUserResponseMock = UserResponseDTO.builder()
				.id(2)
				.username("mati")
				.email("mati@gmail.com")
				.address("street 12324")
				.role("USER")
				.build();
		this.userRequestMock = UserRequestDTO.builder()
				.username("eze")
				.email("eze@gmail.com")
				.address("street 123")
				.password("password123")
				.build();
		this.userDetailsResponseMock = UserDetailsResponseDTO.builder()
				.id(1)
				.email("eze@gmail.com")
				.password("password123")
				.role("USER")
				.build();
		this.roleUpdateRequestMock = RoleUpdateRequestDTO.builder()
				.role("ADMIN")
				.build();
	}
	
	@Test
	public void findAllTest() {
		List<UserResponseDTO> usersResponseMock = List.of(userResponseMock);
		when(service.findAll()).thenReturn(usersResponseMock);
		
		ResponseEntity<List<UserResponseDTO>> responseEntity = controller.findAll();
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(usersResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void findAllDeletedUsers() {
		List<UserResponseDTO> usersResponseMock = List.of(deletedUserResponseMock);
		when(service.findAllDeletedUsers()).thenReturn(usersResponseMock);
		
		ResponseEntity<List<UserResponseDTO>> responseEntity = controller.findAllDeletedUsers();
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(usersResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void findByIdTest() throws NotFoundException {
		when(service.findById(1)).thenReturn(userResponseMock);
		
		ResponseEntity<UserResponseDTO> responseEntity = controller.findById(1);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(userResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void findByEmailNotFoundTest() throws NotFoundException {
		when(service.findByEmail("martin@gmail.com")).thenThrow(new NotFoundException("User", "martin@gmail.com"));
		
		ResponseEntity<UserDetailsResponseDTO> responseEntity = null;
		
		try {
			responseEntity = controller.findByEmail("martin@gmail.com");
		} catch (NotFoundException e) {
			assertNull(responseEntity);
		} catch (Exception e) {
			fail("The test threw another exception instead of NotFoundException");
		}
	}
	
	@Test
	public void findByEmailTest() throws NotFoundException {
		when(service.findByEmail("eze@gmail.com")).thenReturn(userDetailsResponseMock);
		
		ResponseEntity<UserDetailsResponseDTO> responseEntity = controller.findByEmail("eze@gmail.com");
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(userDetailsResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void saveUserTest() throws EmailAlreadyUsedException, NotFoundException {
		when(service.saveUser(userRequestMock)).thenReturn(userResponseMock);
		
		ResponseEntity<UserResponseDTO> responseEntity = controller.saveUser(userRequestMock);
		
		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertEquals(userResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void updateUserRoleTest() throws NotFoundException, InvalidRoleException {
		userResponseMock.setRole("ADMIN");
		when(service.updateUserRole(1, roleUpdateRequestMock)).thenReturn(userResponseMock);
		
		ResponseEntity<UserResponseDTO> responseEntity = controller.updateUserRole(1, roleUpdateRequestMock);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(userResponseMock.getRole(), responseEntity.getBody().getRole());
	}
}
