package com.music.userMS.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.music.userMS.dto.ArtistRequestDTO;
import com.music.userMS.dto.ArtistResponseDTO;
import com.music.userMS.exception.BadParamsException;
import com.music.userMS.exception.NotFoundException;
import com.music.userMS.model.Artist;
import com.music.userMS.repository.ArtistRepository;

@Service(value = "artistService")
public class ArtistService {
	
	@Autowired
	private ArtistRepository repository;
	
	@Transactional(readOnly = true)
	public List<ArtistResponseDTO> findAll() {
		return repository.findAll().stream().map( ArtistResponseDTO::new ).toList();
	}
	
	@Transactional(readOnly = true)
	public ArtistResponseDTO findById(int id) throws NotFoundException {
		Optional<Artist> optional = repository.findById(id);
		if (optional.isPresent()) {
			return new ArtistResponseDTO(optional.get());
		} else {
			throw new NotFoundException("Artist", id);
		}
	}
	
	@Transactional(readOnly = true)
	public List<ArtistResponseDTO> findAllByids(List<Integer> request) throws BadParamsException {
//		List<Integer> ids = null;
//		try {
//			ids = request
//					.stream()
//					.map( Integer::parseInt )
//					.toList();
//		} catch (Exception e) {
//			throw new BadParamsException("Params should be integers");
//		}
                
		return repository.findAllById(request)
				.stream()
				.map( ArtistResponseDTO::new ).toList();
	}
	
	@Transactional
	public ArtistResponseDTO saveArtist(ArtistRequestDTO request) {
		Artist artist = new Artist(request);
		return new ArtistResponseDTO(repository.save(artist));
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