package com.music.musicMS.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.music.musicMS.dto.GenreRequestDTO;
import com.music.musicMS.dto.GenreResponseDTO;
import com.music.musicMS.exception.NameAlreadyUsedException;
import com.music.musicMS.exception.NotFoundException;
import com.music.musicMS.model.Genre;
import com.music.musicMS.repository.GenreRepository;

@Service(value = "genreService")
public class GenreService {

	@Autowired
	private GenreRepository repository;
	
	@Transactional(readOnly = true)
	public List<GenreResponseDTO> findAll() {
		return repository.findAll().stream().map( GenreResponseDTO::new ).toList();
	}
	
	@Transactional(readOnly = true)
	public GenreResponseDTO findById(int id) throws NotFoundException {
		Optional<Genre> optional = repository.findById(id);
		if (optional.isPresent()) {
			return new GenreResponseDTO(optional.get());
		} else {
			throw new NotFoundException("Genre", id);
		}
	}
	
	@Transactional
	public GenreResponseDTO saveGenre(GenreRequestDTO request) throws NameAlreadyUsedException {
		Optional<Genre> optional = repository.findByName(request.getName());
		if (!optional.isPresent()) {
			Genre genre = new Genre(request);
			return new GenreResponseDTO(repository.save(genre));
		} else {
			throw new NameAlreadyUsedException("Genre", request.getName());
		}
	}
	
	@Transactional
	public GenreResponseDTO updateGenre(int id, GenreRequestDTO request) throws NotFoundException {
		Optional<Genre> optional = repository.findById(id);
		if (optional.isPresent()) {
			Genre genre = optional.get();
			genre.setName(request.getName());
			
			return new GenreResponseDTO(repository.save(genre));
		} else {
			throw new NotFoundException("Genre", id);
		}
	}
	
	@Transactional
	public GenreResponseDTO deleteGenre(int id) throws NotFoundException {
		Optional<Genre> optional = repository.findById(id);
		if (optional.isPresent()) {
			Genre genre = optional.get();
			repository.deleteById(id);
			
			return new GenreResponseDTO(genre);
		} else {
			throw new NotFoundException("Genre", id);
		}
	}
}
