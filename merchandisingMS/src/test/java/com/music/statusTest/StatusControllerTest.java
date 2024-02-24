package com.music.statusTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.music.merchandisingMS.controller.StatusController;
import com.music.merchandisingMS.dto.StatusRequestDTO;
import com.music.merchandisingMS.dto.StatusResponseDTO;
import com.music.merchandisingMS.exception.NameAlreadyUsedException;
import com.music.merchandisingMS.exception.NotFoundException;
import com.music.merchandisingMS.service.StatusService;

public class StatusControllerTest {

	@Mock
	private StatusService service;
	
	@InjectMocks
	private StatusController controller;
	
	private StatusResponseDTO statusResponseMock;
	private StatusRequestDTO statusRequestMock;
	
	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
		this.statusResponseMock = StatusResponseDTO.builder()
				.id(1)
				.name("PENDING")
				.build();
		this.statusRequestMock = StatusRequestDTO.builder()
				.name("PENDING")
				.build();
	}
	
	@Test
	public void findAllTest() {
		List<StatusResponseDTO> listResponseMock = List.of(statusResponseMock);
		when(service.findAll()).thenReturn(listResponseMock);
		
		ResponseEntity<List<StatusResponseDTO>> responseEntity = controller.findAll();
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(listResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void findByIdTest() throws NotFoundException {
		when(service.findById(1)).thenReturn(statusResponseMock);
		
		ResponseEntity<StatusResponseDTO> responseEntity = controller.findById(1);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(statusResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void saveStatusTest() throws NameAlreadyUsedException {
		when(service.saveStatus(statusRequestMock)).thenReturn(statusResponseMock);
		
		ResponseEntity<StatusResponseDTO> responseEntity = controller.saveStatus(statusRequestMock);
		
		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertEquals(statusResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void updateStatusTest() throws NameAlreadyUsedException, NotFoundException {
		statusResponseMock.setName("SHIPPED");
		statusRequestMock.setName("SHIPPED");
		when(service.updateStatus(1, statusRequestMock)).thenReturn(statusResponseMock);
		
		ResponseEntity<StatusResponseDTO> responseEntity = controller.updateStatus(1, statusRequestMock);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(statusResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void deleteStatusTest() throws SQLIntegrityConstraintViolationException, NotFoundException {
		when(service.deleteStatus(1)).thenReturn(statusResponseMock);
		
		ResponseEntity<StatusResponseDTO> responseEntity = controller.deleteStatus(1);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(statusResponseMock, responseEntity.getBody());
	}
}
