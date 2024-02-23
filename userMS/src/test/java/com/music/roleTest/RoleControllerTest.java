package com.music.roleTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.music.userMS.controller.RoleController;
import com.music.userMS.dto.RoleRequestDTO;
import com.music.userMS.dto.RoleResponseDTO;
import com.music.userMS.exception.NameAlreadyUsedException;
import com.music.userMS.exception.NotFoundException;
import com.music.userMS.service.RoleService;

public class RoleControllerTest {

	@Mock
	private RoleService service;
	
	@InjectMocks
	private RoleController controller;
	
	private RoleResponseDTO roleResponseMock;
	private RoleRequestDTO roleRequestMock;
	
	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
		this.roleResponseMock = RoleResponseDTO.builder()
			.id(1)
			.name("USER")
			.build();
		this.roleRequestMock = RoleRequestDTO.builder()
			.name("USER")
			.build();
	}
	
	@Test
	public void findAllTest() {
		List<RoleResponseDTO> rolesResponseMock = List.of(roleResponseMock);
		when(service.findAll()).thenReturn(rolesResponseMock);
		
		ResponseEntity<List<RoleResponseDTO>> responseEntity = controller.findAll();
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(rolesResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void findByIdTest() throws NotFoundException {
		when(service.findById(1)).thenReturn(roleResponseMock);
		
		ResponseEntity<RoleResponseDTO> responseEntity = controller.findById(1);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(roleResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void saveRole() throws NameAlreadyUsedException {
		when(service.saveRole(roleRequestMock)).thenReturn(roleResponseMock);
		
		ResponseEntity<RoleResponseDTO> responseEntity = controller.saveRole(roleRequestMock);
		
		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertEquals(roleResponseMock, responseEntity.getBody());
	}
}
