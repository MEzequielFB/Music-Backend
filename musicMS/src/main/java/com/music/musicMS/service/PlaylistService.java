package com.music.musicMS.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.music.musicMS.dto.ArtistResponseDTO;
import com.music.musicMS.dto.PlaylistRequestDTO;
import com.music.musicMS.dto.PlaylistResponseDTO;
import com.music.musicMS.dto.PlaylistUpdateDTO;
import com.music.musicMS.dto.SongResponseDTO;
import com.music.musicMS.dto.UserDTO;
import com.music.musicMS.exception.AlreadyContainsSongException;
import com.music.musicMS.exception.NameAlreadyUsedException;
import com.music.musicMS.exception.NotFoundException;
import com.music.musicMS.model.Playlist;
import com.music.musicMS.model.Song;
import com.music.musicMS.repository.PlaylistRepository;
import com.music.musicMS.repository.SongRepository;

@Service(value = "playlistService")
public class PlaylistService {

	@Autowired
	private PlaylistRepository repository;
	
	@Autowired
	private SongRepository songRepository;
	
	@Autowired
	private WebClient.Builder webClientBuilder;
	
	@Transactional(readOnly = true)
	public List<PlaylistResponseDTO> findAll(String token) {
		return repository.findAll()
				.stream()
				.map(playlist -> {
					UserDTO user = webClientBuilder.build()
							.get()
							.uri("http://localhost:8001/api/user/" + playlist.getUserId())
							.header("Authorization", token)
							.retrieve()
							.bodyToMono(UserDTO.class)
							.block();
					
					PlaylistResponseDTO responseDTO = new PlaylistResponseDTO(playlist);
					responseDTO.setUser(user);
					
					return responseDTO;
				}).toList();
	}
	
	@Transactional(readOnly = true)
	public PlaylistResponseDTO findById(Integer id, String token) throws NotFoundException {
		Optional<Playlist> optional = repository.findById(id);
		if (optional.isPresent()) {
			Playlist playlist = optional.get();
			
			UserDTO user = webClientBuilder.build()
					.get()
					.uri("http://localhost:8001/api/user/" + playlist.getUserId())
					.header("Authorization", token)
					.retrieve()
					.bodyToMono(UserDTO.class)
					.block();
			
			PlaylistResponseDTO responseDTO =  new PlaylistResponseDTO(playlist);
			responseDTO.setUser(user);
					
			return responseDTO;
		} else {
			throw new NotFoundException("Playlist", id);
		}
	}
	
	@Transactional(readOnly = true)
	public List<SongResponseDTO> getSongsFromPlaylist(Integer id, String token) throws NotFoundException {
		Optional<Playlist> optional = repository.findById(id);
		if (!optional.isPresent()) {
			throw new NotFoundException("Playlist", id);
		}
		
		List<SongResponseDTO> songs = repository.getSongsFromPlaylist(id);
		
		for (SongResponseDTO song : songs) {
			List<ArtistResponseDTO> artists = webClientBuilder.build()
			.get()
			.uri(uriBuilder -> uriBuilder
					.scheme("http")
			        .host("localhost")
			        .port(8001)
			        .path("/api/artist/allByIds")
					.queryParam("ids", songRepository.findById(song.getId()).get().getArtists())
					.build())
			.header("Authorization", token)
			.retrieve()
			.bodyToMono(new ParameterizedTypeReference<List<ArtistResponseDTO>>(){})
			.block();
			
			song.setArtists(artists);
		}
		
		return songs;
	}
	
	@Transactional
	public PlaylistResponseDTO savePlaylist(PlaylistRequestDTO request, String token) throws NameAlreadyUsedException, NotFoundException {
		UserDTO user = null;
		try {
			user = webClientBuilder.build()
					.get()
					.uri("http://localhost:8001/api/user/" + request.getUserId())
					.header("Authorization", token)
					.retrieve()
					.bodyToMono(UserDTO.class)
					.block();
		} catch (Exception e) {
			throw new NotFoundException("User", request.getUserId());
//			System.out.println(e.getStackTrace());
		}
		
		//Doesn't allow playlists with the same name from the same user
		Optional<Playlist> optional = repository.findByUserAndName(user.getId(), request.getName());
		if (optional.isPresent()) {
			throw new NameAlreadyUsedException("Playlist", request.getName());
		}
		
		Playlist playlist = new Playlist(request);
		
		PlaylistResponseDTO responseDTO = new PlaylistResponseDTO(repository.save(playlist));
		responseDTO.setUser(user);
		
		return responseDTO;
	}
	
	@Transactional
	public SongResponseDTO addSong(Integer id, Integer songId) throws NotFoundException, AlreadyContainsSongException {
		Optional<Playlist> optional = repository.findById(id);
		Optional<Song> songOptional = songRepository.findById(songId);
		if (!optional.isPresent()) {
			throw new NotFoundException("Playlist", id);
		}
		if (!songOptional.isPresent()) {
			throw new NotFoundException("Song", songId);
		}
		
		Playlist playlist = optional.get();
		Song song = songOptional.get();
		
		songOptional = songRepository.findByPlaylist(songId, playlist);
		if (songOptional.isPresent()) {
			throw new AlreadyContainsSongException("Playlist");
		}
		
		playlist.addSong(song);
		repository.save(playlist);
		
		SongResponseDTO responseDTO =  new SongResponseDTO(song);
		
		return responseDTO;
	}
	
	@Transactional
	public SongResponseDTO removeSong(Integer id, Integer songId) throws NotFoundException, AlreadyContainsSongException {
		Optional<Playlist> optional = repository.findById(id);
		Optional<Song> songOptional = songRepository.findById(songId);
		if (!optional.isPresent()) {
			throw new NotFoundException("Playlist", id);
		}
		if (!songOptional.isPresent()) {
			throw new NotFoundException("Song", songId);
		}
		
		Playlist playlist = optional.get();
		Song song = songOptional.get();
		
		playlist.removeSong(song);
		repository.save(playlist);
		
		SongResponseDTO responseDTO =  new SongResponseDTO(song);
		
		return responseDTO;
	}
	
	@Transactional
	public PlaylistResponseDTO updatePlaylist(Integer id, PlaylistUpdateDTO request, String token) throws NotFoundException {
		Optional<Playlist> optional = repository.findById(id);
		if (optional.isPresent()) {
			Playlist playlist = optional.get();
			playlist.setName(request.getName());
			playlist.setIsPublic(request.getIsPublic());
			
			UserDTO user = webClientBuilder.build()
					.get()
					.uri("http://localhost:8001/api/user/" + playlist.getUserId())
					.header("Authorization", token)
					.retrieve()
					.bodyToMono(UserDTO.class)
					.block();
			
			PlaylistResponseDTO responseDTO =  new PlaylistResponseDTO(repository.save(playlist));
			responseDTO.setUser(user);
					
			return responseDTO;
		} else {
			throw new NotFoundException("Playlist", id);
		}
	}
	
	@Transactional
	public PlaylistResponseDTO deletePlaylist(Integer id, String token) throws NotFoundException {
		Optional<Playlist> optional = repository.findById(id);
		if (optional.isPresent()) {
			Playlist playlist = optional.get();
			repository.deleteById(id);
			
			UserDTO user = webClientBuilder.build()
					.get()
					.uri("http://localhost:8001/api/user/" + playlist.getUserId())
					.header("Authorization", token)
					.retrieve()
					.bodyToMono(UserDTO.class)
					.block();
			
			PlaylistResponseDTO responseDTO =  new PlaylistResponseDTO(playlist);
			responseDTO.setUser(user);
					
			return responseDTO;
		} else {
			throw new NotFoundException("Playlist", id);
		}
	}
}
