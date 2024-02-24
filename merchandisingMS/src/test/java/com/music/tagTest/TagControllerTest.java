package com.music.tagTest;

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

import com.music.merchandisingMS.controller.TagController;
import com.music.merchandisingMS.dto.TagRequestDTO;
import com.music.merchandisingMS.dto.TagResponseDTO;
import com.music.merchandisingMS.exception.NameAlreadyUsedException;
import com.music.merchandisingMS.exception.NoTagsException;
import com.music.merchandisingMS.exception.NotFoundException;
import com.music.merchandisingMS.service.TagService;

public class TagControllerTest {

	@Mock
	private TagService service;
	
	@InjectMocks
	private TagController controller;
	
	private TagResponseDTO tagResponseMock;
	private TagRequestDTO tagRequestMock;
	
	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
		this.tagResponseMock = TagResponseDTO.builder()
				.id(1)
				.name("accesory")
				.build();
		this.tagRequestMock = TagRequestDTO.builder()
				.name("accesory")
				.build();
	}
	
	@Test
	public void findAllTest() {
		List<TagResponseDTO> tagsResponseMock = List.of(tagResponseMock);
		when(service.findAll()).thenReturn(tagsResponseMock);
		
		ResponseEntity<List<TagResponseDTO>> responseEntity = controller.findAll();
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(tagsResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void findByIdTest() throws NotFoundException {
		when(service.findById(1)).thenReturn(tagResponseMock);
		
		ResponseEntity<TagResponseDTO> responseEntity = controller.findById(1);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(tagResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void saveTagTest() throws NameAlreadyUsedException {
		when(service.saveTag(tagRequestMock)).thenReturn(tagResponseMock);
		
		ResponseEntity<TagResponseDTO> responseEntity = controller.saveTag(tagRequestMock);
		
		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertEquals(tagResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void updateTagTest() throws NotFoundException, NameAlreadyUsedException {
		tagResponseMock.setName("t-shirt");
		tagRequestMock.setName("t-shirt");
		when(service.updateTag(1, tagRequestMock)).thenReturn(tagResponseMock);
		
		ResponseEntity<TagResponseDTO> responseEntity = controller.updateTag(1, tagRequestMock);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(tagResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void deleteTagTest() throws NotFoundException, NoTagsException {
		when(service.deleteTag(1)).thenReturn(tagResponseMock);
		
		ResponseEntity<TagResponseDTO> responseEntity = controller.deleteTag(1);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(tagResponseMock, responseEntity.getBody());
	}
}
