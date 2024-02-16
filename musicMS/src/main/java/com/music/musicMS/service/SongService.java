package com.music.musicMS.service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.music.musicMS.dto.SongRequestDTO;
import com.music.musicMS.dto.SongResponseDTO;
import com.music.musicMS.exception.AuthorizationException;
import com.music.musicMS.exception.NameAlreadyUsedException;
import com.music.musicMS.exception.NotFoundException;
import com.music.musicMS.exception.SomeEntityDoesNotExistException;
import com.music.musicMS.model.Album;
import com.music.musicMS.model.Artist;
import com.music.musicMS.model.Genre;
import com.music.musicMS.model.Playlist;
import com.music.musicMS.model.Song;
import com.music.musicMS.repository.AlbumRepository;
import com.music.musicMS.repository.ArtistRepository;
import com.music.musicMS.repository.GenreRepository;
import com.music.musicMS.repository.PlaylistRepository;
import com.music.musicMS.repository.SongRepository;

@Service(value = "songService")
public class SongService {

	@Autowired
	private SongRepository repository;
	
	@Autowired
	private GenreRepository genreRepository;
	
	@Autowired
	private ArtistRepository artistRepository;
	
	@Autowired
	private AlbumRepository albumRepository;
	
	@Autowired
	private PlaylistRepository playlistRepository;
	
	@Autowired
	private WebClient.Builder webClientBuilder;
	
	@Transactional(readOnly = true)
	public List<SongResponseDTO> searchSongs(String name, List<String> genres, List<Integer> years) {
		if (name != null) {
			if (genres == null && years == null) {
				return repository.findByName(name)
						.stream()
						.map( SongResponseDTO::new ).toList();
			} else if (genres != null && years == null) {
				return repository.findByNameAndGenre(name, genres)
						.stream()
						.map( SongResponseDTO::new ).toList();
			} else if (genres == null && years != null) {
				return repository.findByNameAndYear(name, years)
						.stream()
						.map( SongResponseDTO::new ).toList();
			} else {
				return repository.findByNameGenreAndYear(name, genres, years)
						.stream()
						.map( SongResponseDTO::new ).toList();
			}
		} else {
			if (genres != null && years == null) {
				return repository.findByGenre(genres)
						.stream()
						.map( SongResponseDTO::new ).toList();
			} else if (genres == null && years != null) {
				return repository.findByYear(years)
						.stream()
						.map( SongResponseDTO::new ).toList();
			} else {
				return repository.findByGenreAndYear(genres, years)
						.stream()
						.map( SongResponseDTO::new ).toList();
			}
		}
	}
	
	@Transactional(readOnly = true)
	public List<SongResponseDTO> findAllByFilters(List<String> data) {
		return repository.findAllByFilters(data)
				.stream()
				.map(SongResponseDTO::new)
				.toList();
	}
	
	@Transactional(readOnly = true)
	public List<SongResponseDTO> findAll() {
		return repository.findAll()
				.stream()
				.map( SongResponseDTO::new ).toList();
	}
	
	@Transactional(readOnly = true)
	public SongResponseDTO findById(Integer id) throws NotFoundException {
		Optional<Song> optional = repository.findById(id);
		if (!optional.isPresent()) {
			throw new NotFoundException("Song", id);
		}
		Song song = optional.get();
		
		return new SongResponseDTO(song); 
	}
	
	@Transactional
	public SongResponseDTO saveSong(SongRequestDTO request, String token) throws NameAlreadyUsedException, SomeEntityDoesNotExistException, NotFoundException, AuthorizationException {
		Integer loggedUserId = null;
		try {
			loggedUserId = webClientBuilder.build()
					.get()
					.uri("http://localhost:8004/api/auth/id")
					.header("Authorization", token)
					.retrieve()
					.bodyToMono(Integer.class)	
					.block();
		} catch (Exception e) {
			System.err.println(e);
			throw new AuthorizationException();
		}
		
		Optional<Song> optional = repository.findByArtistsAndName(request.getArtists(), request.getName());
		Optional<Album> albumOptional = null;
		Album album = null;
		
		if (request.getAlbumId() != null) {
			albumOptional = albumRepository.findById(request.getAlbumId());
			
			if (!albumOptional.isPresent()) {
				throw new NotFoundException("Album", request.getAlbumId());
			}
			
			album = albumOptional.get();
		}
		
		if (optional.isPresent()) {
			throw new NameAlreadyUsedException("Song", request.getName());
		}
		
		List<Artist> artists = artistRepository.findAllByIds(request.getArtists());
		
		if (artists.size() != request.getArtists().size()) {
			throw new SomeEntityDoesNotExistException("artists");
		}
		
		Boolean containsLoggedUser = false;
		for (Artist artist : artists) {
			if (artist.getUserId().equals(loggedUserId)) {
				containsLoggedUser = true;
				break;
			}
		}
		
		// the logged artist should be in the list of artists to save the song
		if (!containsLoggedUser) {
			throw new AuthorizationException();
		}
		
		List<Genre> genres = genreRepository.findAllById(request.getGenres());
		
		if (genres.size() != request.getGenres().size()) {
			throw new SomeEntityDoesNotExistException("genres");
		}
		
		Song song = new Song(request, album);
		song.setGenres(genres);
		song.setArtists(artists);
		
		//Default date -> current date
		song.setReleaseDate(new Date(System.currentTimeMillis()));
		
		SongResponseDTO responseDTO = new SongResponseDTO(repository.save(song));
		
		return responseDTO;
	}
	
	@Transactional
	public SongResponseDTO updateSong(Integer id, SongRequestDTO request, String token) throws SomeEntityDoesNotExistException, NotFoundException, AuthorizationException {
		Integer loggedUserId = null;
		try {
			loggedUserId = webClientBuilder.build()
					.get()
					.uri("http://localhost:8004/api/auth/id")
					.header("Authorization", token)
					.retrieve()
					.bodyToMono(Integer.class)	
					.block();
		} catch (Exception e) {
			System.err.println(e);
			throw new AuthorizationException();
		}
		
		Optional<Song> optional = repository.findById(id);
		Optional<Album> albumOptional = null;
		
		if (request.getAlbumId() != null) {
			albumOptional = albumRepository.findById(request.getAlbumId());
		}
		
		if (!optional.isPresent()) {
			throw new NotFoundException("Song", id);
		}
		if (albumOptional != null && !albumOptional.isPresent()) {
			throw new NotFoundException("Album", request.getAlbumId());
		}

		Song song = optional.get();
		if (albumOptional != null) {
			Album album = albumOptional.get();
			song.setAlbum(album);
		}
		song.setName(request.getName());
		
		List<Artist> artists = artistRepository.findAllByIds(request.getArtists());
		
		if (artists.size() != request.getArtists().size()) {
			throw new SomeEntityDoesNotExistException("artists");
		}
		
		Boolean containsLoggedUser = false;
		for (Artist artist : artists) {
			if (artist.getUserId().equals(loggedUserId)) {
				containsLoggedUser = true;
				break;
			}
		}
		
		// the logged artist should be in the list of artists to save the song
		if (!containsLoggedUser) {
			throw new AuthorizationException();
		}
		
		song.setArtists(artists);
		
		List<Genre> genres = genreRepository.findAllByIds(request.getGenres());
		
		if (genres.size() != request.getGenres().size()) {
			throw new SomeEntityDoesNotExistException("genres");
		}
		song.setGenres(genres);
		
		SongResponseDTO responseDTO = new SongResponseDTO(repository.save(song));
		
		return responseDTO;
	}
	
	public SongResponseDTO listenSong(Integer id) throws NotFoundException {
		Optional<Song> optional = repository.findById(id);
		
		if (!optional.isPresent()) {
			throw new NotFoundException("Song", id);
		}
		
		Song song = optional.get();
		song.addReproduction();
		
		return new SongResponseDTO(repository.save(song));
	}
	
	@Transactional
	public SongResponseDTO deleteSong(Integer id) throws NotFoundException {
		Optional<Song> optional = repository.findById(id);
		if (!optional.isPresent()) {
			throw new NotFoundException("Song", id);
		}
			
		Song song = optional.get();
		List<Playlist> playlists = playlistRepository.findAllBySong(song);
		
		for (Playlist playlist : playlists) {
			playlist.removeSong(song);
		}
		
		repository.deleteById(id);
		
		SongResponseDTO responseDTO = new SongResponseDTO(song);
		
		return responseDTO;
	}
}
