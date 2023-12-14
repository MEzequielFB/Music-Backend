package com.music.musicMS.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.music.musicMS.dto.AddSongRequestDTO;
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
	public List<PlaylistResponseDTO> findAll() {
		return repository.findAll()
				.stream()
				.map(playlist -> {
					UserDTO user = webClientBuilder.build()
							.get()
							.uri("http://localhost:8001/api/user/" + playlist.getUserId())
							.retrieve()
							.bodyToMono(UserDTO.class)
							.block();
					
					PlaylistResponseDTO responseDTO = new PlaylistResponseDTO(playlist);
					responseDTO.setUser(user);
					
					return responseDTO;
				}).toList();
	}
	
	@Transactional(readOnly = true)
	public PlaylistResponseDTO findById(int id) throws NotFoundException {
		Optional<Playlist> optional = repository.findById(id);
		if (optional.isPresent()) {
			Playlist playlist = optional.get();
			
			UserDTO user = webClientBuilder.build()
					.get()
					.uri("http://localhost:8001/api/user/" + playlist.getUserId())
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
	public List<SongResponseDTO> getSongsFromPlaylist(int id) throws NotFoundException {
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
			.retrieve()
			.bodyToMono(new ParameterizedTypeReference<List<ArtistResponseDTO>>(){})
			.block();
			
			song.setArtists(artists);
		}
		
		return songs;
	}
	
	@Transactional
	public PlaylistResponseDTO savePlaylist(PlaylistRequestDTO request) throws NameAlreadyUsedException, NotFoundException {
		UserDTO user = null;
		try {
			user = webClientBuilder.build()
					.get()
					.uri("http://localhost:8001/api/user/" + request.getUserId())
					.retrieve()
					.bodyToMono(UserDTO.class)
					.block();
		} catch (Exception e) {
			throw new NotFoundException("User", request.getUserId());
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
	public SongResponseDTO addSong(int id, AddSongRequestDTO request) throws NotFoundException, AlreadyContainsSongException {
		Optional<Playlist> optional = repository.findById(id);
		Optional<Song> songOptional = songRepository.findById(request.getSongId());
		if (!optional.isPresent()) {
			throw new NotFoundException("Playlist", id);
		}
		if (!songOptional.isPresent()) {
			throw new NotFoundException("Song", request.getSongId());
		}
		
		Playlist playlist = optional.get();
		Song song = songOptional.get();
		
		Optional<Song> songOptional2 = songRepository.findByPlaylist(request.getSongId(), playlist);
		if (songOptional2.isPresent()) {
			throw new AlreadyContainsSongException("Playlist");
		}
		
		playlist.addSong(song);
		repository.save(playlist);
		
		List<ArtistResponseDTO> artists = webClientBuilder.build()
				.get()
				.uri(uriBuilder -> uriBuilder
						.scheme("http")
				        .host("localhost")
				        .port(8001)
				        .path("/api/artist/allByIds")
						.queryParam("ids", song.getArtists())
						.build())
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<List<ArtistResponseDTO>>(){})
				.block();
		
		SongResponseDTO responseDTO =  new SongResponseDTO(song);
		responseDTO.setArtists(artists);
		
		return responseDTO;
	}
	
	@Transactional
	public PlaylistResponseDTO updatePlaylist(int id, PlaylistUpdateDTO request) throws NotFoundException {
		Optional<Playlist> optional = repository.findById(id);
		if (optional.isPresent()) {
			Playlist playlist = optional.get();
			playlist.setName(request.getName());
			playlist.setIsPublic(request.getIsPublic());
			
			UserDTO user = webClientBuilder.build()
					.get()
					.uri("http://localhost:8001/api/user/" + playlist.getUserId())
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
	public PlaylistResponseDTO deletePlaylist(int id) throws NotFoundException {
		Optional<Playlist> optional = repository.findById(id);
		if (optional.isPresent()) {
			Playlist playlist = optional.get();
			repository.deleteById(id);
			
			UserDTO user = webClientBuilder.build()
					.get()
					.uri("http://localhost:8001/api/user/" + playlist.getUserId())
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
