package com.music.musicMS.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.music.musicMS.dto.AlbumRequestDTO;
import com.music.musicMS.dto.AlbumResponseDTO;
import com.music.musicMS.dto.AlbumUpdateDTO;
import com.music.musicMS.dto.UserDTO;
import com.music.musicMS.exception.AlbumOwnerNotInSongException;
import com.music.musicMS.exception.AuthorizationException;
import com.music.musicMS.exception.DoNotContainsTheSongException;
import com.music.musicMS.exception.NameAlreadyUsedException;
import com.music.musicMS.exception.NotFoundException;
import com.music.musicMS.exception.PermissionsException;
import com.music.musicMS.exception.SongIsAlreadyInAnAlbumException;
import com.music.musicMS.model.Album;
import com.music.musicMS.model.Artist;
import com.music.musicMS.model.Roles;
import com.music.musicMS.model.Song;
import com.music.musicMS.repository.AlbumRepository;
import com.music.musicMS.repository.ArtistRepository;
import com.music.musicMS.repository.SongRepository;

@Service(value = "albumService")
public class AlbumService {

	@Autowired
	private AlbumRepository repository;
	
	@Autowired
	private ArtistRepository artistRepository;
	
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
	public List<AlbumResponseDTO> findAll() {
		return repository.findAll().stream().map( AlbumResponseDTO::new ).toList();
	}
	
	@Transactional(readOnly = true)
	public List<AlbumResponseDTO> findAllByFilter(String data) {
		return repository.findAllByFilter(data)
				.stream()
				.map(AlbumResponseDTO::new)
				.toList();
	}
	
	@Transactional(readOnly = true)
	public AlbumResponseDTO findById(Integer id) throws NotFoundException {
		Optional<Album> optional = repository.findById(id);
		if (optional.isPresent()) {
			Album album = optional.get();
			return new AlbumResponseDTO(album);
		} else {
			throw new NotFoundException("Album", id);
		}
	}
	
	@Transactional(readOnly = true)
	public List<AlbumResponseDTO> findAllByOwner(Integer ownerId) throws NotFoundException {
		Optional<Artist> artistOptional = artistRepository.findById(ownerId);
		if (artistOptional.isPresent()) {
			Artist artist = artistOptional.get();
			return artist.getOwnedAlbums()
					.stream()
					.map( AlbumResponseDTO::new ).toList();
		} else {
			throw new NotFoundException("Artist", ownerId);
		}
	}
	
	@Transactional
	public AlbumResponseDTO saveAlbum(AlbumRequestDTO request, String token) throws NotFoundException, NameAlreadyUsedException, AuthorizationException {
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
		
		Optional<Artist> artistOptional = artistRepository.findByUserId(loggedUserId);
		if (!artistOptional.isPresent()) {
			throw new NotFoundException("Artist", loggedUserId);
		}
		
		Artist owner = artistOptional.get();
		
		Optional<Album> albumOptional = repository.findByNameAndOwner(request.getName(), owner);
		if (albumOptional.isPresent()) {
			throw new NameAlreadyUsedException("Album", request.getName());
		}
		
		Album album = new Album(request);
		album.setOwner(owner);
		
		return new AlbumResponseDTO(repository.save(album));
	}
	
	@Transactional
	public AlbumResponseDTO updateAlbum(Integer id, AlbumUpdateDTO request, String token) throws NotFoundException, AuthorizationException, PermissionsException, NameAlreadyUsedException {
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
		
		Optional<Artist> artistOptional = artistRepository.findByUserId(loggedUserId);
		if (!artistOptional.isPresent()) {
			throw new NotFoundException("Artist", loggedUserId);
		}
		
		Artist artist = artistOptional.get();
		
		Optional<Album> optional = repository.findByNameAndOwner(request.getName(), artist);
		if (optional.isPresent() && !optional.get().getId().equals(id)) {
			throw new NameAlreadyUsedException("Album", request.getName());
		}
		
		optional = repository.findById(id);
		if (!optional.isPresent()) {
			throw new NotFoundException("Album", id);
		}
		
		Album album = optional.get();
		if (!album.getOwner().getUserId().equals(loggedUserId)) {
			throw new PermissionsException(loggedUserId);
		}
		
		album.setName(request.getName());
		
		return new AlbumResponseDTO(repository.save(album));
	}
	
	@Transactional
	public AlbumResponseDTO addSong(Integer id, Integer songId, String token) throws NotFoundException, SongIsAlreadyInAnAlbumException, AlbumOwnerNotInSongException, AuthorizationException, PermissionsException {
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
		
		Optional<Album> optional = repository.findById(id);
		Optional<Song> songOptional = songRepository.findById(songId);
		
		if (!optional.isPresent()) {
			throw new NotFoundException("Album", id);
		}
		if (!songOptional.isPresent()) {
			throw new NotFoundException("Song", songId);
		}
		
		Album album = optional.get();
		Song song = songOptional.get();
	
		if (!album.getOwner().getUserId().equals(loggedUserId)) {
			throw new PermissionsException(loggedUserId);
		}
		if (song.getAlbum() != null) {
			throw new SongIsAlreadyInAnAlbumException(song.getName(), song.getAlbum().getName());
		}
		if (!songRepository.songContainsArtist(song, album.getOwner())) {
			throw new AlbumOwnerNotInSongException(song.getName(), album.getOwner().getName());
		}
		
		song.setAlbum(album);
		song = songRepository.save(song);
		
		album.addSong(song);
		
		Set<Artist> artists = songRepository.findArtistsBySongs(album.getSongs());
		album.setArtists(artists);
		
		return new AlbumResponseDTO(repository.save(album));
	}
	
	@Transactional
	public AlbumResponseDTO removeSong(Integer id, Integer songId, String token) throws NotFoundException, DoNotContainsTheSongException, AuthorizationException, PermissionsException {
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
		
		Optional<Album> optional = repository.findById(id);
		Optional<Song> songOptional = songRepository.findById(songId);
		
		if (!optional.isPresent()) {
			throw new NotFoundException("Album", id);
		}
		if (!songOptional.isPresent()) {
			throw new NotFoundException("Song", songId);
		}
		
		Album album = optional.get();
		Song song = songOptional.get();
		
		if (!album.getOwner().getUserId().equals(loggedUserId)) {
			throw new PermissionsException(loggedUserId);
		}
		if (song.getAlbum() != null && song.getAlbum().equals(album)) {
			song.setAlbum(null);
			album.removeSong(song);
		} else {
			throw new DoNotContainsTheSongException(album, song.getName());
		}

		Set<Artist> artists = songRepository.findArtistsBySongs(album.getSongs());
		album.setArtists(artists);
		
		return new AlbumResponseDTO(repository.save(album));
	}
	
	@Transactional
	public AlbumResponseDTO deleteAlbum(Integer id, String token) throws NotFoundException, AuthorizationException, PermissionsException {
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
		}
		
		Optional<Album> optional = repository.findById(id);
		if (!optional.isPresent()) {
			throw new NotFoundException("Album", id);
		}
		
		Album album = optional.get();
		
		if (!album.getOwner().getUserId().equals(loggedUserId) && (!user.getRole().equals(Roles.ADMIN) && !user.getRole().equals(Roles.SUPER_ADMIN))) {
			throw new PermissionsException(loggedUserId);
		}
		
		album.getArtists().clear();
		songRepository.removeSongsFromAlbum(album);
		
		repository.deleteById(id);
		
		return new AlbumResponseDTO(album);
	}
}
