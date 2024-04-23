package com.music.musicMS.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.music.musicMS.dto.PlaylistRequestDTO;
import com.music.musicMS.dto.PlaylistResponseDTO;
import com.music.musicMS.dto.PlaylistUpdateDTO;
import com.music.musicMS.dto.SongResponseDTO;
import com.music.musicMS.dto.UserDTO;
import com.music.musicMS.exception.AlreadyContainsSongException;
import com.music.musicMS.exception.AuthorizationException;
import com.music.musicMS.exception.DoNotContainsTheSongException;
import com.music.musicMS.exception.NameAlreadyUsedException;
import com.music.musicMS.exception.NotFoundException;
import com.music.musicMS.exception.PermissionsException;
import com.music.musicMS.model.Playlist;
import com.music.musicMS.model.Roles;
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
	private WebClient webClient;
	
	@Value("${app.api.domain}")
	private String domain;
	
	@Value("${app.api.authms.domain}")
	private String authmsDomain;
	
	@Value("${app.api.userms.domain}")
	private String usermsDomain;
	
	@Transactional(readOnly = true)
	public List<PlaylistResponseDTO> findAll(String token) {
		return repository.findAllByPublic()
				.stream()
				.map(playlist -> {
					UserDTO user = webClient
							.get()
							.uri(String.format("%s/api/user/%s", this.usermsDomain, playlist.getUserId()))
							.header("Authorization", token)
							.retrieve()
							.bodyToMono(UserDTO.class)
							.block();
					
					PlaylistResponseDTO responseDTO = new PlaylistResponseDTO(playlist, user);
					
					return responseDTO;
				}).toList();
	}
	
	@Transactional(readOnly = true)
	public List<PlaylistResponseDTO> findAllByName(String name, String token) {
		if (name == null || name.isEmpty()) {
			return repository.findAllByPublic()
					.stream()
					.map(playlist -> {
						UserDTO user = webClient
								.get()
								.uri(String.format("%s/api/user/%s", this.usermsDomain, playlist.getUserId()))
								.header("Authorization", token)
								.retrieve()
								.bodyToMono(UserDTO.class)
								.block();
						
						PlaylistResponseDTO responseDTO = new PlaylistResponseDTO(playlist, user);
						
						return responseDTO;
					}).toList();
		}
		return repository.findAllByName(name)
				.stream()
				.map(playlist -> {
					UserDTO user = webClient
							.get()
							.uri(String.format("%s/api/user/%s", this.usermsDomain, playlist.getUserId()))
							.header("Authorization", token)
							.retrieve()
							.bodyToMono(UserDTO.class)
							.block();
					
					PlaylistResponseDTO responseDTO = new PlaylistResponseDTO(playlist, user);
					
					return responseDTO;
				})
				.toList();
	}
	
	@Transactional(readOnly = true)
	public List<PlaylistResponseDTO> findAllByLoggedUser(String token) throws AuthorizationException {
		Integer loggedUserId = null;
		try {
			loggedUserId = webClient
					.get()
					.uri(String.format("%s/api/auth/id", this.authmsDomain))
					.header("Authorization", token)
					.retrieve()
					.bodyToMono(Integer.class)
					.block();
		} catch (Exception e) {
			System.err.println(e);
			throw new AuthorizationException();
		}
		
		return repository.findAllByUserId(loggedUserId)
				.stream()
				.map(playlist -> {
					UserDTO user = webClient
							.get()
							.uri(String.format("%s/api/user/%s", this.usermsDomain, playlist.getUserId()))
							.header("Authorization", token)
							.retrieve()
							.bodyToMono(UserDTO.class)
							.block();
					
					PlaylistResponseDTO responseDTO = new PlaylistResponseDTO(playlist, user);
					
					return responseDTO;
				})
				.toList();
	}
	
	@Transactional(readOnly = true)
	public PlaylistResponseDTO findById(Integer id, String token) throws NotFoundException, AuthorizationException {
		Integer loggedUserId = null;
		try {
			loggedUserId = webClient
					.get()
					.uri(String.format("%s/api/auth/id", this.authmsDomain))
					.header("Authorization", token)
					.retrieve()
					.bodyToMono(Integer.class)
					.block();
		} catch (Exception e) {
			System.err.println(e);
			throw new AuthorizationException();
		}
		
		Optional<Playlist> optional = repository.findById(id);
		if (!optional.isPresent()) {
			throw new NotFoundException("Playlist", id);
		}
		
		Playlist playlist = optional.get();
		
		if (!playlist.getIsPublic() && !playlist.getUserId().equals(loggedUserId)) {
			throw new NotFoundException("Playlist", id);
		}
		
		UserDTO user = null;
		try {
			user = webClient
					.get()
					.uri(String.format("%s/api/user/%s", this.usermsDomain, playlist.getUserId()))
					.header("Authorization", token)
					.retrieve()
					.bodyToMono(UserDTO.class)
					.block();
		} catch (Exception e) {
			throw new NotFoundException("User", playlist.getUserId());
		}
		
		PlaylistResponseDTO responseDTO =  new PlaylistResponseDTO(playlist, user);
				
		return responseDTO;
	}
	
	@Transactional(readOnly = true)
	public List<SongResponseDTO> getSongsFromPlaylist(Integer id, String token) throws NotFoundException, AuthorizationException {
		Integer loggedUserId = null;
		try {
			loggedUserId = webClient
					.get()
					.uri(String.format("%s/api/auth/id", this.authmsDomain))
					.header("Authorization", token)
					.retrieve()
					.bodyToMono(Integer.class)	
					.block();
		} catch (Exception e) {
			System.err.println(e);
			throw new AuthorizationException();
		}
		
		Optional<Playlist> optional = repository.findById(id);
		if (!optional.isPresent()) {
			throw new NotFoundException("Playlist", id);
		}
		
		Playlist playlist = optional.get();
		
		if (!playlist.getIsPublic() && !loggedUserId.equals(playlist.getUserId())) {
			throw new NotFoundException("Playlist", id);
		}
		List<SongResponseDTO> songs = repository.getSongsFromPlaylist(playlist);
		
		return songs;
	}
	
	@Transactional
	public PlaylistResponseDTO savePlaylist(PlaylistRequestDTO request, String token) throws NameAlreadyUsedException, NotFoundException, AuthorizationException {
		Integer loggedUserId = null;
		try {
			loggedUserId = webClient
					.get()
					.uri(String.format("%s/api/auth/id", this.authmsDomain))
					.header("Authorization", token)
					.retrieve()
					.bodyToMono(Integer.class)	
					.block();
		} catch (Exception e) {
			System.err.println(e);
			throw new AuthorizationException();
		}
		
		UserDTO user = null;
		try {
			user = webClient
					.get()
					.uri(String.format("%s/api/user/%s", this.usermsDomain, loggedUserId))
					.header("Authorization", token)
					.retrieve()
					.bodyToMono(UserDTO.class)
					.block();
		} catch (Exception e) {
			throw new NotFoundException("User", loggedUserId);
//			System.out.println(e.getStackTrace());
		}
		
		//Doesn't allow playlists with the same name from the same user
		Optional<Playlist> optional = repository.findByUserAndName(user.getId(), request.getName());
		if (optional.isPresent()) {
			throw new NameAlreadyUsedException("Playlist", request.getName());
		}
		
		Playlist playlist = new Playlist(request, loggedUserId);
		
		PlaylistResponseDTO responseDTO = new PlaylistResponseDTO(repository.save(playlist), user);
		
		return responseDTO;
	}
	
	@Transactional
	public SongResponseDTO addSong(Integer id, Integer songId, String token) throws NotFoundException, AlreadyContainsSongException, AuthorizationException, PermissionsException {
		Integer loggedUserId = null;
		try {
			loggedUserId = webClient
					.get()
					.uri(String.format("%s/api/auth/id", this.authmsDomain))
					.header("Authorization", token)
					.retrieve()
					.bodyToMono(Integer.class)	
					.block();
		} catch (Exception e) {
			System.err.println(e);
			throw new AuthorizationException();
		}
		
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
		
		if (!playlist.getUserId().equals(loggedUserId)) {
			throw new PermissionsException(loggedUserId);
		}
		
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
	public SongResponseDTO removeSong(Integer id, Integer songId, String token) throws NotFoundException, AlreadyContainsSongException, AuthorizationException, DoNotContainsTheSongException, PermissionsException {
		Integer loggedUserId = null;
		try {
			loggedUserId = webClient
					.get()
					.uri(String.format("%s/api/auth/id", this.authmsDomain))
					.header("Authorization", token)
					.retrieve()
					.bodyToMono(Integer.class)	
					.block();
		} catch (Exception e) {
			System.err.println(e);
			throw new AuthorizationException();
		}
		
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
		
		if (!playlist.getUserId().equals(loggedUserId)) {
			throw new PermissionsException(loggedUserId);
		}
		
		songOptional = songRepository.findByPlaylist(songId, playlist);
		if (!songOptional.isPresent()) {
			throw new DoNotContainsTheSongException(playlist, song.getName());
		}
		
		playlist.removeSong(song);
		repository.save(playlist);
		
		SongResponseDTO responseDTO =  new SongResponseDTO(song);
		
		return responseDTO;
	}
	
	@Transactional
	public PlaylistResponseDTO updatePlaylist(Integer id, PlaylistUpdateDTO request, String token) throws NotFoundException, AuthorizationException, PermissionsException {
		Integer loggedUserId = null;
		try {
			loggedUserId = webClient
					.get()
					.uri(String.format("%s/api/auth/id", this.authmsDomain))
					.header("Authorization", token)
					.retrieve()
					.bodyToMono(Integer.class)	
					.block();
		} catch (Exception e) {
			System.err.println(e);
			throw new AuthorizationException();
		}
		
		Optional<Playlist> optional = repository.findById(id);
		if (!optional.isPresent()) {
			throw new NotFoundException("Playlist", id);
		}
		
		Playlist playlist = optional.get();
		
		if (!playlist.getUserId().equals(loggedUserId)) {
			throw new PermissionsException(loggedUserId);
		}
		
		playlist.setName(request.getName());
		playlist.setIsPublic(request.getIsPublic());
		
		UserDTO user = null;
		try {
			user = webClient
					.get()
					.uri(String.format("%s/api/user/%s", this.usermsDomain, playlist.getUserId()))
					.header("Authorization", token)
					.retrieve()
					.bodyToMono(UserDTO.class)
					.block();
		} catch (Exception e) {
			throw new NotFoundException("User", playlist.getId());
		}
		
		PlaylistResponseDTO responseDTO =  new PlaylistResponseDTO(repository.save(playlist), user);
				
		return responseDTO;
	}
	
	@Transactional
	public PlaylistResponseDTO deletePlaylist(Integer id, String token) throws NotFoundException, AuthorizationException, PermissionsException {
		Integer loggedUserId = null;
		try {
			loggedUserId = webClient
					.get()
					.uri(String.format("%s/api/auth/id", this.authmsDomain))
					.header("Authorization", token)
					.retrieve()
					.bodyToMono(Integer.class)	
					.block();
		} catch (Exception e) {
			System.err.println(e);
			throw new AuthorizationException();
		}
		
		UserDTO loggedUser = null;
		try {
			loggedUser = webClient
					.get()
					.uri(String.format("%s/api/user/%s", this.usermsDomain, loggedUserId))
					.header("Authorization", token)
					.retrieve()
					.bodyToMono(UserDTO.class)
					.block();
		} catch (Exception e) {
			throw new NotFoundException("User", loggedUserId);
		}
		
		Optional<Playlist> optional = repository.findById(id);
		if (!optional.isPresent()) {
			throw new NotFoundException("Playlist", id);
		}
		
		Playlist playlist = optional.get();
		
		if (!playlist.getUserId().equals(loggedUserId) && (!loggedUser.getRole().equals(Roles.ADMIN) && !loggedUser.getRole().equals(Roles.SUPER_ADMIN))) {
			throw new PermissionsException(loggedUserId);
		}
		
		repository.deleteById(id);
		
		UserDTO user = null;
		try {
			user = webClient
					.get()
					.uri(String.format("%s/api/user/%s", this.usermsDomain, playlist.getUserId()))
					.header("Authorization", token)
					.retrieve()
					.bodyToMono(UserDTO.class)
					.block();
		} catch (Exception e) {
			throw new NotFoundException("User", playlist.getUserId());
		}
		
		PlaylistResponseDTO responseDTO =  new PlaylistResponseDTO(playlist, user);
				
		return responseDTO;
	}
}