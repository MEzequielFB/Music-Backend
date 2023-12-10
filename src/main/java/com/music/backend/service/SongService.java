package com.music.backend.service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.music.backend.dto.SongRequestDTO;
import com.music.backend.dto.SongResponseDTO;
import com.music.backend.exception.NameAlreadyUsedException;
import com.music.backend.exception.NotFoundException;
import com.music.backend.exception.SomeEntityDoesNotExistException;
import com.music.backend.model.Artist;
import com.music.backend.model.Genre;
import com.music.backend.model.Song;
import com.music.backend.repository.ArtistRepository;
import com.music.backend.repository.GenreRepository;
import com.music.backend.repository.SongRepository;

@Service(value = "songService")
public class SongService {

	@Autowired
	private SongRepository repository;
	
	@Autowired
	private ArtistRepository artistRepository;
	
	@Autowired
	private GenreRepository genreRepository;
	
	@Transactional(readOnly = true)
	public List<SongResponseDTO> searchSongs(String name, List<String> genres, List<Integer> years) {
		if (name != null) {
			if (genres == null && years == null) {
				return repository.findByName(name).stream().map( SongResponseDTO::new ).toList();
			} else if (genres != null && years == null) {
				return repository.findByNameAndGenre(name, genres).stream().map( SongResponseDTO::new ).toList();
			} else if (genres == null && years != null) {
				return repository.findByNameAndYear(name, years).stream().map( SongResponseDTO::new ).toList();
			} else {
				return repository.findByNameGenreAndYear(name, genres, years).stream().map( SongResponseDTO::new ).toList();
			}
		} else {
			if (genres != null && years == null) {
				return repository.findByGenre(genres).stream().map( SongResponseDTO::new ).toList();
			} else if (genres == null && years != null) {
				return repository.findByYear(years).stream().map( SongResponseDTO::new ).toList();
			} else {
				return repository.findByGenreAndYear(genres, years).stream().map( SongResponseDTO::new ).toList();
			}
		}
	}
	
	@Transactional(readOnly = true)
	public List<SongResponseDTO> findAll() {
		return repository.findAll().stream().map( SongResponseDTO::new ).toList();
	}
	
	@Transactional(readOnly = true)
	public SongResponseDTO findById(int id) throws NotFoundException {
		Optional<Song> optional = repository.findById(id);
		if (optional.isPresent()) {
			return new SongResponseDTO(optional.get());
		} else {
			throw new NotFoundException("Song", id);
		}
	}
	
	@Transactional
	public SongResponseDTO saveSong(SongRequestDTO request) throws NameAlreadyUsedException, SomeEntityDoesNotExistException {
		Optional<Song> optional = repository.findByArtistsAndName(request.getArtists(), request.getName());
//		List<Artist> artists = null;
//		List<Genre> genres = null;
		
		// Fail if some or all ids are not found, no entities are returned for these IDs
//		try {
//			artists = artistRepository.findAllById(request.getArtists()
//					.stream()
//					.map(artist -> artist.getId()).toList());
//			
//			genres = genreRepository.findAllById(request.getGenres()
//					.stream()
//					.map(genre -> genre.getId()).toList());
//		} catch (IllegalArgumentException e) {
//			throw new SomeArtistDoesNotExistException();
//		}
		List<Artist> artists = artistRepository.findAllById(request.getArtists()
				.stream()
				.map(artist -> artist.getId()).toList());
		
		if (artists.size() != request.getArtists().size()) {
			throw new SomeEntityDoesNotExistException("artists");
		}
		
		List<Genre> genres = genreRepository.findAllById(request.getGenres()
				.stream()
				.map(genre -> genre.getId()).toList());
		
		if (genres.size() != request.getGenres().size()) {
			throw new SomeEntityDoesNotExistException("genres");
		}
		
		if (optional.isPresent()) {
			throw new NameAlreadyUsedException("Song", request.getName());
		}
		
		Song song = new Song(request);
		song.setArtists(artists);
		song.setGenres(genres);
		
		//Default date -> current date
		song.setReleaseDate(new Date(System.currentTimeMillis()));
		
		return new SongResponseDTO(repository.save(song));
	}
	
	@Transactional
	public SongResponseDTO updateSong(int id, SongRequestDTO request) throws NotFoundException {
		Optional<Song> optional = repository.findById(id);
		if (optional.isPresent()) {
			Song song = optional.get();
			song.setName(request.getName());
			song.setAlbum(request.getAlbum());
			song.setArtists(request.getArtists());
			song.setGenres(request.getGenres());
			
			return new SongResponseDTO(repository.save(song));
		} else {
			throw new NotFoundException("Song", id);
		}
	}
	
	@Transactional
	public SongResponseDTO deleteSong(int id) throws NotFoundException {
		Optional<Song> optional = repository.findById(id);
		if (optional.isPresent()) {
			Song song = optional.get();
			repository.deleteById(id);
			
			return new SongResponseDTO(song);
		} else {
			throw new NotFoundException("Song", id);
		}
	}
}
