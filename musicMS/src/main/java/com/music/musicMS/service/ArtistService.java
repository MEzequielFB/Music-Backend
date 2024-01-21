package com.music.musicMS.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.music.musicMS.dto.ArtistRequestDTO;
import com.music.musicMS.dto.ArtistResponseDTO;
import com.music.musicMS.dto.NameRequestDTO;
import com.music.musicMS.exception.NameAlreadyUsedException;
import com.music.musicMS.exception.NotFoundException;
import com.music.musicMS.model.Artist;
import com.music.musicMS.repository.ArtistRepository;

@Service(value = "artistService")
public class ArtistService {
	
	@Autowired
	private ArtistRepository repository;
	
	@Transactional(readOnly = true)
	public List<ArtistResponseDTO> findAll() {
		return repository.findAllNotDeleted()
				.stream()
				.map( ArtistResponseDTO::new )
				.toList();
	}
	
	@Transactional(readOnly = true)
	public List<ArtistResponseDTO> findAllDeleted() {
		return repository.findAllDeleted()
				.stream()
				.map( ArtistResponseDTO::new )
				.toList();
	}
	
	@Transactional(readOnly = true)
	public ArtistResponseDTO findById(Integer id) throws NotFoundException {
		Optional<Artist> optional = repository.findById(id);
		if (optional.isPresent() && !optional.get().getIsDeleted()) {
			return new ArtistResponseDTO(optional.get());
		} else {
			throw new NotFoundException("Artist", id);
		}
	}
	
	@Transactional(readOnly = true)
	public List<ArtistResponseDTO> findAllByids(List<Integer> ids) {
		return repository.findAllByIds(ids)
				.stream()
				.map( ArtistResponseDTO::new ).toList();
	}
	
	@Transactional
	public ArtistResponseDTO saveArtist(ArtistRequestDTO request) throws NameAlreadyUsedException {
		Optional<Artist> optional = repository.findByName(request.getName());
		if (optional.isPresent()) {
			throw new NameAlreadyUsedException("Artist", request.getName());
		}
		
		Artist artist = new Artist(request);
		return new ArtistResponseDTO(repository.save(artist));
	}
	
	// REMOVE IT (?
	@Transactional
	public ArtistResponseDTO updateArtist(Integer id, NameRequestDTO request) throws NotFoundException {
		Optional<Artist> optional = repository.findById(id);
		if (optional.isPresent() && !optional.get().getIsDeleted()) {
			Artist artist = optional.get();
			artist.setName(request.getName());
			
			return new ArtistResponseDTO(repository.save(artist));
		} else {
			throw new NotFoundException("Artist", id);
		}
	}
	
	@Transactional
	public ArtistResponseDTO deleteArtist(Integer id) throws NotFoundException {
		Optional<Artist> optional = repository.findById(id);
		if (optional.isPresent() && !optional.get().getIsDeleted()) {
			Artist artist = optional.get();
			artist.setIsDeleted(true);
			
			return new ArtistResponseDTO(repository.save(artist));
		} else {
			throw new NotFoundException("Artist", id);
		}
	}

	public ArtistResponseDTO deleteArtistByUserId(Integer userId) throws NotFoundException {
		Optional<Artist> optional = repository.findByUserId(userId);
		if (optional.isPresent() && !optional.get().getIsDeleted()) {
			Artist artist = optional.get();
			artist.setIsDeleted(true);
			
			return new ArtistResponseDTO(repository.save(artist));
		} else {
			throw new NotFoundException("Artist", userId);
		}
	}
}
