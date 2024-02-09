package com.music.songTest;

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

import com.music.musicMS.controller.SongController;
import com.music.musicMS.dto.ArtistResponseDTO;
import com.music.musicMS.dto.GenreResponseDTO;
import com.music.musicMS.dto.SongRequestDTO;
import com.music.musicMS.dto.SongResponseDTO;
import com.music.musicMS.exception.AuthorizationException;
import com.music.musicMS.exception.NameAlreadyUsedException;
import com.music.musicMS.exception.NotFoundException;
import com.music.musicMS.exception.SomeEntityDoesNotExistException;
import com.music.musicMS.service.SongService;

public class SongControllerTest {

	@Mock
	private SongService service;
	
	@InjectMocks
	private SongController controller;
	
	private SongResponseDTO songResponseMock;
	private SongRequestDTO songRequestMock;
	private String tokenMock;
	
	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
		this.songResponseMock = SongResponseDTO.builder()
				.id(1)
				.name("song1")
				.artists(List.of(ArtistResponseDTO.builder().id(1).name("artist1").build()))
				.genres(List.of(GenreResponseDTO.builder().id(1).name("genre1").build()))
				.build();
		this.songRequestMock = SongRequestDTO.builder()
				.name("song1")
				.albumId(null)
				.artists(List.of(1))
				.genres(List.of(1))
				.build();
		this.tokenMock = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdXBlcmFkbWluQGdtYWlsLmNvbSIsImlkIjoyMywiYXV0aCI6IlNVUEVSX0FETUlOIiwiZXhwIjoxNzA3NTMyNDU1fQ.JWKKgNu9xZs6c0lr7ZeRd2v_FFCyTle7XpbumUMeQXPLZYJ1TfTydpESzImAnbljMgZ-OrKCVTHytbVyl4edbQ";
	}
	
	@Test
	public void findAllTest() {
		List<SongResponseDTO> songsResponseMock = List.of(songResponseMock); 
		when(service.findAll()).thenReturn(songsResponseMock);
		
		ResponseEntity<List<SongResponseDTO>> responseEntity = controller.findAll();
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(songsResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void findByIdTest() throws NotFoundException {
		when(service.findById(songResponseMock.getId())).thenReturn(songResponseMock);
		
		ResponseEntity<SongResponseDTO> responseEntity = controller.findById(songResponseMock.getId());
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(songResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void saveSongTest() throws NameAlreadyUsedException, SomeEntityDoesNotExistException, NotFoundException, AuthorizationException {
		when(service.saveSong(songRequestMock, tokenMock)).thenReturn(songResponseMock);
		
		ResponseEntity<SongResponseDTO> responseEntity = controller.saveSong(songRequestMock, tokenMock);
		
		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertEquals(songResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void updateSongTest() throws SomeEntityDoesNotExistException, NotFoundException, AuthorizationException {
		when(service.updateSong(songResponseMock.getId(), songRequestMock, tokenMock)).thenReturn(songResponseMock);
		
		ResponseEntity<SongResponseDTO> responseEntity = controller.updateSong(songResponseMock.getId(), songRequestMock, tokenMock);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(songResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void deleteSongTest() throws NotFoundException {
		when(service.deleteSong(songResponseMock.getId())).thenReturn(songResponseMock);
		
		ResponseEntity<SongResponseDTO> responseEntity = controller.deleteSong(songResponseMock.getId());
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(songResponseMock, responseEntity.getBody());
	}
}
