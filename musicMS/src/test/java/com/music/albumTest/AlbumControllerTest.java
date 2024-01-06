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
import com.music.musicMS.exception.DoNotContainsTheSongException;
import com.music.musicMS.exception.NameAlreadyUsedException;
import com.music.musicMS.exception.NotFoundException;
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
			.ownerId(1)
			.build();
		this.albumUpdateMock = AlbumUpdateDTO.builder()
			.name("album1")
			.build();
		this.songIdMock = SongIdDTO.builder()
			.songId(1)
			.build();
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
	public void saveAlbumTest() throws NotFoundException, NameAlreadyUsedException {
		when(service.saveAlbum(albumRequestMock)).thenReturn(albumResponseMock);
		
		ResponseEntity<AlbumResponseDTO> responseEntity = controller.saveAlbum(albumRequestMock);
		
		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertEquals(albumResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void updateAlbumTest() throws NotFoundException, SomeEntityDoesNotExistException {
		when(service.updateAlbum(albumResponseMock.getId(), albumUpdateMock)).thenReturn(albumResponseMock);
		
		ResponseEntity<AlbumResponseDTO> responseEntity = controller.updateAlbum(albumResponseMock.getId(), albumUpdateMock);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(albumResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void addSongTest() throws NotFoundException, SongIsAlreadyInAnAlbumException, AlbumOwnerNotInSongException {
		when(service.addSong(albumResponseMock.getId(), songIdMock)).thenReturn(albumResponseMock);
		
		ResponseEntity<AlbumResponseDTO> responseEntity = controller.addSong(albumResponseMock.getId(), songIdMock);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(albumResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void removeSongTest() throws NotFoundException, DoNotContainsTheSongException {
		when(service.removeSong(albumResponseMock.getId(), songIdMock)).thenReturn(albumResponseMock);
		
		ResponseEntity<AlbumResponseDTO> responseEntity = controller.removeSong(albumResponseMock.getId(), songIdMock);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(albumResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void deleteAlbumTest() throws NotFoundException {
		when(service.deleteAlbum(albumResponseMock.getId())).thenReturn(albumResponseMock);
		
		ResponseEntity<AlbumResponseDTO> responseEntity = controller.deleteAlbum(albumResponseMock.getId());
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(albumResponseMock, responseEntity.getBody());
	}
}
