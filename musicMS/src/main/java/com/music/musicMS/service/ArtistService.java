package com.music.musicMS.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.music.musicMS.dto.ArtistRequestDTO;
import com.music.musicMS.dto.ArtistResponseDTO;
import com.music.musicMS.dto.NameRequestDTO;
import com.music.musicMS.exception.AuthorizationException;
import com.music.musicMS.exception.NameAlreadyUsedException;
import com.music.musicMS.exception.NotFoundException;
import com.music.musicMS.model.Artist;
import com.music.musicMS.repository.ArtistRepository;

@Service(value = "artistService")
public class ArtistService {
	
	@Autowired
	private ArtistRepository repository;
	
	@Autowired
	private WebClient.Builder webClientBuilder;
	
	@Transactional(readOnly = true)
	public List<ArtistResponseDTO> findAll() {
		return repository.findAllNotDeleted()
				.stream()
				.map( ArtistResponseDTO::new )
				.toList();
	}
	
	@Transactional(readOnly = true)
	public List<ArtistResponseDTO> findAllByName(String name) {
		if (name == null || name.isEmpty()) {
			return repository.findAllNotDeleted()
					.stream()
					.map( ArtistResponseDTO::new )
					.toList();
		}
		
		return repository.findAllByName(name);
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
	
	@Transactional
	public ArtistResponseDTO updateArtist(Integer id, NameRequestDTO request, String token) throws NotFoundException, AuthorizationException {
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
		
		Optional<Artist> optional = repository.findById(id);
		
		if (!optional.isPresent() || optional.get().getIsDeleted()) {
			throw new NotFoundException("Artist", id);
		}
		
		Artist artist = optional.get();
		
		if (!artist.getUserId().equals(loggedUserId)) {
			throw new AuthorizationException();
		}
		
		artist.setName(request.getName());
		
		return new ArtistResponseDTO(repository.save(artist));
	}
	
	public ArtistResponseDTO updateArtistByUserId(Integer userId, NameRequestDTO request, String token) throws NotFoundException, AuthorizationException {
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
		
		Optional<Artist> optional = repository.findByUserId(userId);
		
		if (!optional.isPresent() || optional.get().getIsDeleted()) {
			throw new NotFoundException("Artist", userId);
		}
		
		Artist artist = optional.get();
		
		if (!artist.getUserId().equals(loggedUserId)) {
			throw new AuthorizationException();
		}
		
		artist.setName(request.getName());
		
		return new ArtistResponseDTO(repository.save(artist));
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

	@Transactional
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
