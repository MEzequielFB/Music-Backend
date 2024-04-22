package com.music.genreTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.music.musicMS.controller.GenreController;
import com.music.musicMS.dto.GenreRequestDTO;
import com.music.musicMS.dto.GenreResponseDTO;
import com.music.musicMS.exception.NameAlreadyUsedException;
import com.music.musicMS.exception.NotFoundException;
import com.music.musicMS.model.Genre;
import com.music.musicMS.service.GenreService;

public class GenreControllerTest {

	@Mock
	private GenreService service;
	
	@InjectMocks
	private GenreController controller;
	
	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	public void findAllTest() {
		Genre genreMock = new Genre(1, "rock", new ArrayList<>()); 
		List<GenreResponseDTO> genresResponseMock = List.of(new GenreResponseDTO(genreMock));
		
		when(service.findAll()).thenReturn(genresResponseMock);
		
		ResponseEntity<List<GenreResponseDTO>> responseEntity = controller.findAll();
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(genresResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void findByIdTest() throws NotFoundException {
		Genre genreMock = new Genre(1, "rock", new ArrayList<>());
		GenreResponseDTO genreResponseMock = new GenreResponseDTO(genreMock);
		
		when(service.findById(1)).thenReturn(genreResponseMock);
		
		ResponseEntity<GenreResponseDTO> responseEntity = controller.findById(1);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(genreResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void saveGenreTest() throws NameAlreadyUsedException {
		GenreRequestDTO genreRequestMock = new GenreRequestDTO("rock");
		GenreResponseDTO genreResponseMock = new GenreResponseDTO(1, "rock");
		
		when(service.saveGenre(genreRequestMock)).thenReturn(genreResponseMock);
		
		ResponseEntity<GenreResponseDTO> responseEntity = controller.saveGenre(genreRequestMock);
		
		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertEquals(genreResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void updateGenreTest() throws NotFoundException, NameAlreadyUsedException {
		GenreRequestDTO genreRequestMock = new GenreRequestDTO("rock");
		GenreResponseDTO genreResponseMock = new GenreResponseDTO(1, "rock");
		
		when(service.updateGenre(1, genreRequestMock)).thenReturn(genreResponseMock);
		
		ResponseEntity<GenreResponseDTO> responseEntity = controller.updateGenre(1, genreRequestMock);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(genreResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void deleteGenreTest() throws NotFoundException {
		GenreResponseDTO genreResponseMock = new GenreResponseDTO(1, "rock");
		
		when(service.deleteGenre(1)).thenReturn(genreResponseMock);
		
		ResponseEntity<GenreResponseDTO> responseEntity = controller.deleteGenre(1);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(genreResponseMock, responseEntity.getBody());
	}
}
