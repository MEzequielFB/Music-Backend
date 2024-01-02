package com.music.userMS.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.music.userMS.dto.ArtistRequestDTO;
import com.music.userMS.dto.ArtistResponseDTO;
import com.music.userMS.dto.UserRequestDTO;
import com.music.userMS.dto.UserResponseDTO;
import com.music.userMS.exception.EmailAlreadyUsedException;
import com.music.userMS.exception.NameAlreadyUsedException;
import com.music.userMS.exception.NotFoundException;
import com.music.userMS.model.User;
import com.music.userMS.repository.UserRepository;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@Service(value = "userService")
public class UserService {
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private WebClient.Builder webClientBuilder;
	
	@Transactional(readOnly = true)
	public List<UserResponseDTO> findFollowedUsersById(int id) throws NotFoundException {
		Optional<User> optional = repository.findById(id);
		if (optional.isPresent()) {
			return repository.findFollowedUsersById(id).stream().map( UserResponseDTO::new ).toList();
		} else {
			throw new NotFoundException("User", id);
		}
	}
	
	@Transactional(readOnly = true)
	public List<UserResponseDTO> findFollowersById(int id) throws NotFoundException {
		Optional<User> optional = repository.findById(id);
		if (optional.isPresent()) {
			return repository.findFollowersById(id).stream().map( UserResponseDTO::new ).toList();
		} else {
			throw new NotFoundException("User", id);
		}
	}

	@Transactional(readOnly = true)
	public List<UserResponseDTO> findAll() {
		return repository.findAll().stream().map( UserResponseDTO::new ).toList();
	}
	
	@Transactional(readOnly = true)
	public UserResponseDTO findById(int id) throws NotFoundException {
		Optional<User> optional = repository.findById(id);
		if (optional.isPresent()) {
			return new UserResponseDTO(optional.get());
		} else {
			throw new NotFoundException("User", id);
		}
	}
	
	@Transactional
	public UserResponseDTO saveUser(UserRequestDTO request) throws EmailAlreadyUsedException {
		Optional<User> optional = repository.findByEmail(request.getEmail());
		if (!optional.isPresent()) {
			User user = new User(request);
			//default value
			user.setRole(0);
			
			return new UserResponseDTO(repository.save(user));
		} else {
			throw new EmailAlreadyUsedException(request.getEmail());
		}
	}
	
	@Transactional
	public UserResponseDTO saveArtistUser(UserRequestDTO request) throws EmailAlreadyUsedException {
		Optional<User> optional = repository.findByEmail(request.getEmail());
		if (!optional.isPresent()) {
			User user = new User(request);
			//artist role
			user.setRole(1);
			
			UserResponseDTO userDTO = new UserResponseDTO(repository.save(user));
			System.out.println(userDTO);
			
			webClientBuilder.build()
					.post()
					.uri("http://localhost:8002/api/artist")
					.contentType(MediaType.APPLICATION_JSON)
					.bodyValue(new ArtistRequestDTO(userDTO.getUsername(), userDTO.getId()))
					.retrieve()
					.bodyToMono(ArtistResponseDTO.class)
					.onErrorResume(Exception.class, ex -> {
						System.err.println(ex);
						return Mono.error(ex);
					})
					.block();
			
			return userDTO;
		} else {
			throw new EmailAlreadyUsedException(request.getEmail());
		}
	}
	
	@Transactional
	public UserResponseDTO updateUser(int id, UserRequestDTO request) throws NotFoundException {
		Optional<User> optional = repository.findById(id);
		if (optional.isPresent()) {
			User user = optional.get();
			user.setUsername(request.getUsername());
			user.setEmail(request.getEmail());
			user.setPassword(request.getPassword());
			
			return new UserResponseDTO(repository.save(user));
		} else {
			throw new NotFoundException("User", id);
		}
	}
	
	@Transactional
	public UserResponseDTO deleteUser(int id) throws NotFoundException {
		Optional<User> optional = repository.findById(id);
		if (optional.isPresent()) {
			User user = optional.get();
			repository.deleteById(id);
			
			return new UserResponseDTO(user);
		} else {
			throw new NotFoundException("User", id);
		}
	}
}
