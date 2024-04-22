package com.music.albumTest;

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

import com.music.musicMS.controller.AlbumController;
import com.music.musicMS.dto.AlbumRequestDTO;
import com.music.musicMS.dto.AlbumResponseDTO;
import com.music.musicMS.dto.AlbumUpdateDTO;
import com.music.musicMS.dto.ArtistResponseDTO;
import com.music.musicMS.dto.SongIdDTO;
import com.music.musicMS.exception.AlbumOwnerNotInSongException;
import com.music.musicMS.exception.AuthorizationException;
import com.music.musicMS.exception.DoNotContainsTheSongException;
import com.music.musicMS.exception.NameAlreadyUsedException;
import com.music.musicMS.exception.NotFoundException;
import com.music.musicMS.exception.PermissionsException;
import com.music.musicMS.exception.SomeEntityDoesNotExistException;
import com.music.musicMS.exception.SongIsAlreadyInAnAlbumException;
import com.music.musicMS.service.AlbumService;

public class AlbumControllerTest {
	
	@Mock
	private AlbumService service;
	
	@InjectMocks
	private AlbumController controller;
	
	private ArtistResponseDTO artistResponseMock;
	private AlbumResponseDTO albumResponseMock;
	private AlbumRequestDTO albumRequestMock;
	private AlbumUpdateDTO albumUpdateMock;
	private SongIdDTO songIdMock;
	private String tokenMock;
	
	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
		this.artistResponseMock = ArtistResponseDTO.builder()
			.id(1)
			.name("artist1")
			.build();
		this.albumResponseMock = AlbumResponseDTO.builder()
			.id(1)
			.name("album1")
			.owner(artistResponseMock)
			.artists(List.of(artistResponseMock))
			.songs(List.of())
			.build();
		this.albumRequestMock = AlbumRequestDTO.builder()
			.name("album1")
			.build();
		this.albumUpdateMock = AlbumUpdateDTO.builder()
			.name("album1")
			.build();
		this.songIdMock = SongIdDTO.builder()
			.songId(1)
			.build();
		this.tokenMock = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdXBlcmFkbWluQGdtYWlsLmNvbSIsImlkIjoyMywiYXV0aCI6IlNVUEVSX0FETUlOIiwiZXhwIjoxNzA3NTMyNDU1fQ.JWKKgNu9xZs6c0lr7ZeRd2v_FFCyTle7XpbumUMeQXPLZYJ1TfTydpESzImAnbljMgZ-OrKCVTHytbVyl4edbQ";
	}
	
	@Test
	public void findAllTest() {
		List<AlbumResponseDTO> albumsResponseMock = List.of(albumResponseMock);
		
		when(service.findAll()).thenReturn(albumsResponseMock);
		
		ResponseEntity<List<AlbumResponseDTO>> responseEntity = controller.findAll();
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(albumsResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void findByIdTest() throws NotFoundException {
		when(service.findById(albumResponseMock.getId())).thenReturn(albumResponseMock);
		
		ResponseEntity<AlbumResponseDTO> responseEntity = controller.findById(albumResponseMock.getId());
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(albumResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void findAllByOwnerTest() throws NotFoundException {
		List<AlbumResponseDTO> albumsResponseMock = List.of(albumResponseMock);
		when(service.findAllByOwner(artistResponseMock.getId())).thenReturn(albumsResponseMock);
		
		ResponseEntity<List<AlbumResponseDTO>> responseEntity = controller.findAllByOwner(artistResponseMock.getId());
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(albumsResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void saveAlbumTest() throws NotFoundException, NameAlreadyUsedException, AuthorizationException {
		when(service.saveAlbum(albumRequestMock, tokenMock)).thenReturn(albumResponseMock);
		
		ResponseEntity<AlbumResponseDTO> responseEntity = controller.saveAlbum(albumRequestMock, tokenMock);
		
		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertEquals(albumResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void updateAlbumTest() throws NotFoundException, SomeEntityDoesNotExistException, AuthorizationException, PermissionsException, NameAlreadyUsedException {
		when(service.updateAlbum(albumResponseMock.getId(), albumUpdateMock, tokenMock)).thenReturn(albumResponseMock);
		
		ResponseEntity<AlbumResponseDTO> responseEntity = controller.updateAlbum(albumResponseMock.getId(), albumUpdateMock, tokenMock);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(albumResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void addSongTest() throws NotFoundException, SongIsAlreadyInAnAlbumException, AlbumOwnerNotInSongException, AuthorizationException, PermissionsException {
		when(service.addSong(albumResponseMock.getId(), songIdMock.getSongId(), tokenMock)).thenReturn(albumResponseMock);
		
		ResponseEntity<AlbumResponseDTO> responseEntity = controller.addSong(albumResponseMock.getId(), songIdMock, tokenMock);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(albumResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void removeSongTest() throws NotFoundException, DoNotContainsTheSongException, AuthorizationException, PermissionsException {
		when(service.removeSong(albumResponseMock.getId(), songIdMock.getSongId(), tokenMock)).thenReturn(albumResponseMock);
		
		ResponseEntity<AlbumResponseDTO> responseEntity = controller.removeSong(albumResponseMock.getId(), songIdMock, tokenMock);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(albumResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void deleteAlbumTest() throws NotFoundException, AuthorizationException, PermissionsException {
		when(service.deleteAlbum(albumResponseMock.getId(), tokenMock)).thenReturn(albumResponseMock);
		
		ResponseEntity<AlbumResponseDTO> responseEntity = controller.deleteAlbum(albumResponseMock.getId(), tokenMock);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(albumResponseMock, responseEntity.getBody());
	}
}
