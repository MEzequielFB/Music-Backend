package com.music.playlistTest;

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

import com.music.musicMS.controller.PlaylistController;
import com.music.musicMS.dto.ArtistResponseDTO;
import com.music.musicMS.dto.GenreResponseDTO;
import com.music.musicMS.dto.PlaylistRequestDTO;
import com.music.musicMS.dto.PlaylistResponseDTO;
import com.music.musicMS.dto.PlaylistUpdateDTO;
import com.music.musicMS.dto.SongIdDTO;
import com.music.musicMS.dto.SongResponseDTO;
import com.music.musicMS.dto.UserDTO;
import com.music.musicMS.exception.AlreadyContainsSongException;
import com.music.musicMS.exception.NameAlreadyUsedException;
import com.music.musicMS.exception.NotFoundException;
import com.music.musicMS.model.Artist;
import com.music.musicMS.model.Genre;
import com.music.musicMS.service.PlaylistService;

public class PlaylistControllerTest {

	@Mock
	private PlaylistService service;
	
	@InjectMocks
	private PlaylistController controller;
	
	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	public void findAllTest() {
		UserDTO userDTO = new UserDTO(1, "username", "email@gmail.com", "USER");
		List<PlaylistResponseDTO> playlistsResponseMock =  List.of(new PlaylistResponseDTO(1, "playlist1", false, userDTO));
		
		when(service.findAll()).thenReturn(playlistsResponseMock);
		
		ResponseEntity<List<PlaylistResponseDTO>> responseEntity = controller.findAll();
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(playlistsResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void findByIdTest() throws NotFoundException {
		UserDTO userDTO = new UserDTO(1, "username", "email@gmail.com", "USER");
		PlaylistResponseDTO playlistResponseMock =  new PlaylistResponseDTO(1, "playlist1", false, userDTO);
		
		when(service.findById(1)).thenReturn(playlistResponseMock);
		
		ResponseEntity<PlaylistResponseDTO> responseEntity = controller.findById(1);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(playlistResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void getSongsFromPlaylistTest() throws NotFoundException {
		Genre genre = new Genre(1, "rock", List.of());
		Artist artist = new Artist(1, 1, "artist1", false, List.of(), List.of(), List.of());
		List<SongResponseDTO> songsResponseMock = List.of(new SongResponseDTO(1, "song1", 5, 120, "album1", List.of(new ArtistResponseDTO(artist)), List.of(new GenreResponseDTO(genre))));
		
		when(service.getSongsFromPlaylist(1)).thenReturn(songsResponseMock);
		
		ResponseEntity<List<SongResponseDTO>> responseEntity = controller.getSongsFromPlaylist(1);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(songsResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void savePlaylistTest() throws NameAlreadyUsedException, NotFoundException {
		UserDTO userDTO = new UserDTO(1, "username", "email@gmail.com", "USER");
		PlaylistRequestDTO playlistRequestMock = new PlaylistRequestDTO("playlist1", false, 1);
		PlaylistResponseDTO playlistResponseMock = new PlaylistResponseDTO(1, "playlist1", false, userDTO);
		
		when(service.savePlaylist(playlistRequestMock)).thenReturn(playlistResponseMock);
		
		ResponseEntity<PlaylistResponseDTO> responseEntity = controller.savePlaylist(playlistRequestMock);
		
		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertEquals(playlistResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void updatePlaylistTest() throws NotFoundException {
		UserDTO userDTO = new UserDTO(1, "username", "email@gmail.com", "USER");
		PlaylistUpdateDTO playlistUpdateMock = new PlaylistUpdateDTO("new name", false);
		PlaylistResponseDTO playlistResponseMock = new PlaylistResponseDTO(1, "new name", false, userDTO);
		
		when(service.updatePlaylist(1, playlistUpdateMock)).thenReturn(playlistResponseMock);
		
		ResponseEntity<PlaylistResponseDTO> responseEntity = controller.updatePlaylist(1, playlistUpdateMock);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(playlistResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void addSongTest() throws NotFoundException, AlreadyContainsSongException {
		Genre genre = new Genre(1, "rock", List.of());
		Artist artist = new Artist(1, 1, "artist1", false, List.of(), List.of(), List.of());
		SongIdDTO songRequestMock = new SongIdDTO(1);
		SongResponseDTO songResponseMock = new SongResponseDTO(1, "song1", 5, 120, "album1", List.of(new ArtistResponseDTO(artist)), List.of(new GenreResponseDTO(genre)));
		
		when(service.addSong(1, songRequestMock.getSongId())).thenReturn(songResponseMock);
		
		ResponseEntity<SongResponseDTO> responseEntity = controller.addSong(1, songRequestMock);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(songResponseMock, responseEntity.getBody());
	}
	
	@Test
	public void deletePlaylistTest() throws NotFoundException {
		UserDTO userDTO = new UserDTO(1, "username", "email@gmail.com", "USER");
		PlaylistResponseDTO playlistResponseMock = new PlaylistResponseDTO(1, "playlist1", false, userDTO);
		
		when(service.deletePlaylist(1)).thenReturn(playlistResponseMock);
		
		ResponseEntity<PlaylistResponseDTO> responseEntity = controller.deletePlaylist(1);
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(playlistResponseMock, responseEntity.getBody());
	}
}
