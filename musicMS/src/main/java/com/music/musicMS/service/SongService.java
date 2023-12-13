package com.music.musicMS.service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

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
	public List<SongResponseDTO> findAll() {
		return repository.findAll()
				.stream()
				.map( SongResponseDTO::new ).toList();
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
		List<ArtistResponseDTO> artists = null;
		
		try {
			artists = webClientBuilder.build()
					.get()
					.uri(uriBuilder -> uriBuilder
							.path("http://localhost:8001/api/artist/allByIds")
							.queryParam("ids", request.getArtists())
							.build())
					.retrieve()
					.bodyToMono(new ParameterizedTypeReference<List<ArtistResponseDTO>>(){})
					.block();
		} catch (Exception e) {
			throw e;
		}
		
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
		
		//Default date -> current date
		song.setReleaseDate(new Date(System.currentTimeMillis()));
		
		SongResponseDTO responseDTO = new SongResponseDTO(repository.save(song));
		responseDTO.setArtists(artists);
		
		return responseDTO;
	}
	
	@Transactional
	public SongResponseDTO updateSong(int id, SongRequestDTO request) throws NotFoundException {
		Optional<Song> optional = repository.findById(id);
		if (optional.isPresent()) {
			Song song = optional.get();
			song.setName(request.getName());
			song.setAlbum(request.getAlbum());
			
			try {
				List<ArtistResponseDTO> artists = webClientBuilder.build()
						.get()
						.uri(uriBuilder -> uriBuilder
								.path("http://localhost:8001/api/artist/allByIds")
								.queryParam("ids", request.getArtists())
								.build())
						.retrieve()
						.bodyToMono(new ParameterizedTypeReference<List<ArtistResponseDTO>>(){})
						.block();
				song.setArtists(request.getArtists());
				song.setGenres(request.getGenres());
				
				SongResponseDTO responseDTO = new SongResponseDTO(repository.save(song));
				responseDTO.setArtists(artists);
				
				return responseDTO;
			} catch (Exception e) {
				throw e;
			}
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
