package com.music.artistTest;

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

import com.music.musicMS.controller.ArtistController;
import com.music.musicMS.dto.ArtistRequestDTO;
import com.music.musicMS.dto.ArtistResponseDTO;
import com.music.musicMS.dto.NameRequestDTO;
import com.music.musicMS.exception.AuthorizationException;
import com.music.musicMS.exception.NameAlreadyUsedException;
import com.music.musicMS.exception.NotFoundException;
import com.music.musicMS.exception.PermissionsException;
import com.music.musicMS.service.ArtistService;

public class ArtistControllerTest {

	@Mock
	private ArtistService service;
	
	@InjectMocks
	private ArtistController controller;
	
	private ArtistResponseDTO artistResponseMock;
	private ArtistRequestDTO artistRequestMock;
	private NameRequestDTO nameRequestMock;
	private String tokenMock;
	
	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
		this.artistResponseMock = ArtistResponseDTO.builder()
			.id(1)
			.name("artist1")
			.build();
		this.artistRequestMock = ArtistRequestDTO.builder()
			.name("artist1")
			.build();
		this.nameRequestMock = NameRequestDTO.builder()
			.name("newArtistName")
			.build();
		this.tokenMock = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdXBlcmFkbWluQGdtYWlsLmNvbSIsImlkIjoyMywiYXV0aCI6IlNVUEVSX0FETUlOIiwiZXhwIjoxNzA3NTMyNDU1fQ.JWKKgNu9xZs6c0lr7ZeRd2v_FFCyTle7XpbumUMeQXPLZYJ1TfTydpESzImAnbljMgZ-OrKCVTHytbVyl4edbQ";
	}
	
	@Test
	public void findAllTest() {
		List<ArtistResponseDTO> artistsResponseMock = List.of(artistResponseMock);
		when(service.findAll()).thenReturn(artistsResponseMock);
		
		ResponseEntity<List<ArtistResponseDTO>> responseEntity = controller.findAll();
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(artistsResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void findByIdTest() throws NotFoundException {
		when(service.findById(artistResponseMock.getId())).thenReturn(artistResponseMock);
		
		ResponseEntity<ArtistResponseDTO> responseEntity = controller.findById(artistResponseMock.getId());
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(artistResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void findAllByIds() {
		List<ArtistResponseDTO> artistsResponseMock = List.of(artistResponseMock);
		List<Integer> idsMock = List.of(artistResponseMock.getId());
		when(service.findAllByids(idsMock)).thenReturn(artistsResponseMock);
		
		ResponseEntity<List<ArtistResponseDTO>> responseEntity = controller.findAllByIds(idsMock);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(artistsResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void saveArtistTest() throws NameAlreadyUsedException {
		when(service.saveArtist(artistRequestMock)).thenReturn(artistResponseMock);
		
		ResponseEntity<ArtistResponseDTO> responseEntity = controller.saveArtist(artistRequestMock);
		
		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertEquals(artistResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void updateArtistTest() throws NotFoundException, AuthorizationException, PermissionsException, NameAlreadyUsedException {
		when(service.updateArtist(artistResponseMock.getId(), nameRequestMock, tokenMock)).thenReturn(artistResponseMock);
		
		ResponseEntity<ArtistResponseDTO> responseEntity = controller.updateArtist(artistResponseMock.getId(), nameRequestMock, tokenMock);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(artistResponseMock, responseEntity.getBody());
	}
	
	@Test
	public  void deleteArtistTest() throws NotFoundException {
		when(service.deleteArtist(artistResponseMock.getId())).thenReturn(artistResponseMock);
		
		ResponseEntity<ArtistResponseDTO> responseEntity = controller.deleteArtist(artistResponseMock.getId());
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(artistResponseMock, responseEntity.getBody());
	}
}
