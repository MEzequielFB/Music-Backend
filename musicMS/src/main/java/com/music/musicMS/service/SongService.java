package com.music.musicMS.service;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.music.musicMS.dto.ArtistResponseDTO;
import com.music.musicMS.dto.SongRequestDTO;
import com.music.musicMS.dto.SongResponseDTO;
import com.music.musicMS.exception.NameAlreadyUsedException;
import com.music.musicMS.exception.NotFoundException;
import com.music.musicMS.exception.SomeEntityDoesNotExistException;
import com.music.musicMS.model.Genre;
import com.music.musicMS.model.Song;
import com.music.musicMS.repository.GenreRepository;
import com.music.musicMS.repository.SongRepository;

@Service(value = "songService")
public class SongService {

	@Autowired
	private SongRepository repository;
	
	@Autowired
	private GenreRepository genreRepository;
	
	@Transactional(readOnly = true)
	public List<SongResponseDTO> searchSongs(String name, List<String> genres, List<Integer> years) {
		if (name != null) {
			if (genres == null && years == null) {
				return repository.findByName(name)
						.stream()
						.map(song -> {
							List<ArtistResponseDTO> artistsDTO = new LinkedList<>(); // TRAER CON WEBCLIENT
							return new SongResponseDTO(song, artistsDTO);
						}).toList();
			} else if (genres != null && years == null) {
				return repository.findByNameAndGenre(name, genres)
						.stream()
						.map(song -> {
							List<ArtistResponseDTO> artistsDTO = new LinkedList<>(); // TRAER CON WEBCLIENT
							return new SongResponseDTO(song, artistsDTO);
						}).toList();
			} else if (genres == null && years != null) {
				return repository.findByNameAndYear(name, years)
						.stream()
						.map(song -> {
							List<ArtistResponseDTO> artistsDTO = new LinkedList<>(); // TRAER CON WEBCLIENT
							return new SongResponseDTO(song, artistsDTO);
						}).toList();
			} else {
				return repository.findByNameGenreAndYear(name, genres, years)
						.stream()
						.map(song -> {
							List<ArtistResponseDTO> artistsDTO = new LinkedList<>(); // TRAER CON WEBCLIENT
							return new SongResponseDTO(song, artistsDTO);
						}).toList();
			}
		} else {
			if (genres != null && years == null) {
				return repository.findByGenre(genres)
						.stream()
						.map(song -> {
							List<ArtistResponseDTO> artistsDTO = new LinkedList<>(); // TRAER CON WEBCLIENT
							return new SongResponseDTO(song, artistsDTO);
						}).toList();
			} else if (genres == null && years != null) {
				return repository.findByYear(years)
						.stream()
						.map(song -> {
							List<ArtistResponseDTO> artistsDTO = new LinkedList<>(); // TRAER CON WEBCLIENT
							return new SongResponseDTO(song, artistsDTO);
						}).toList();
			} else {
				return repository.findByGenreAndYear(genres, years)
						.stream()
						.map(song -> {
							List<ArtistResponseDTO> artistsDTO = new LinkedList<>(); // TRAER CON WEBCLIENT
							return new SongResponseDTO(song, artistsDTO);
						}).toList();
			}
		}
	}
	
	@Transactional(readOnly = true)
	public List<SongResponseDTO> findAll() {
		return repository.findAll()
				.stream()
				.map(song -> {
					List<ArtistResponseDTO> artistsDTO = new LinkedList<>(); // TRAER CON WEBCLIENT
					return new SongResponseDTO(song, artistsDTO);
				}).toList();
	}
	
	@Transactional(readOnly = true)
	public SongResponseDTO findById(int id) throws NotFoundException {
		Optional<Song> optional = repository.findById(id);
		if (optional.isPresent()) {
			List<ArtistResponseDTO> artists = new LinkedList<>(); // TRAER CON WEBCLIENT
			return new SongResponseDTO(optional.get(), artists);
		} else {
			throw new NotFoundException("Song", id);
		}
	}
	
	@Transactional
	public SongResponseDTO saveSong(SongRequestDTO request) throws NameAlreadyUsedException, SomeEntityDoesNotExistException {
		Optional<Song> optional = repository.findByArtistsAndName(request.getArtists(), request.getName());

		List<ArtistResponseDTO> artists = new LinkedList<>(); // TRAER CON WEBCLIENT
		
//		List<Artist> artists = artistRepository.findAllById(request.getArtists()
//				.stream()
//				.map(artist -> artist.getId()).toList());
		
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
		
		return new SongResponseDTO(repository.save(song), artists);
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
			
			List<ArtistResponseDTO> artists = new LinkedList<>(); // TRAER CON WEBCLIENT
			
			return new SongResponseDTO(repository.save(song), artists);
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
			
			List<ArtistResponseDTO> artists = new LinkedList<>(); // TRAER CON WEBCLIENT
			
			return new SongResponseDTO(song, artists);
		} else {
			throw new NotFoundException("Song", id);
		}
	}
}
