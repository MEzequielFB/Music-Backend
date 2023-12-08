package com.music.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.music.backend.dto.ArtistRequestDTO;
import com.music.backend.dto.ArtistResponseDTO;
import com.music.backend.exception.NotFoundException;
import com.music.backend.model.Artist;
import com.music.backend.repository.ArtistRepository;

import jakarta.transaction.Transactional;

@Service(value = "artistService")
public class ArtistService {
	
	@Autowired
	private ArtistRepository repository;
	
	@Transactional
	public List<ArtistResponseDTO> findAll() {
		return repository.findAll().stream().map( ArtistResponseDTO::new ).toList();
	}
	
	@Transactional
	public ArtistResponseDTO findById(int id) throws NotFoundException {
		Optional<Artist> optional = repository.findById(id);
		if (optional.isPresent()) {
			return new ArtistResponseDTO(optional.get());
		} else {
			throw new NotFoundException("Artist", id);
		}
	}
	
	@Transactional
	public ArtistResponseDTO saveArtist(ArtistRequestDTO request) {
		Artist artist = new Artist(request);
		return new ArtistResponseDTO(artist);
	}
	
	@Transactional
	public ArtistResponseDTO updateArtist(int id, ArtistRequestDTO request) throws NotFoundException {
		Optional<Artist> optional = repository.findById(id);
		if (optional.isPresent()) {
			Artist artist = optional.get();
			artist.setName(request.getName());
			artist.setPassword(request.getPassword());
			
			return new ArtistResponseDTO(repository.save(artist));
		} else {
			throw new NotFoundException("Artist", id);
		}
	}
	
	@Transactional
	public ArtistResponseDTO deleteArtist(int id) throws NotFoundException {
		Optional<Artist> optional = repository.findById(id);
		if (optional.isPresent()) {
			Artist artist = optional.get();
			repository.deleteById(id);
			
			return new ArtistResponseDTO(artist);
		} else {
			throw new NotFoundException("Artist", id);
		}
	}
}
