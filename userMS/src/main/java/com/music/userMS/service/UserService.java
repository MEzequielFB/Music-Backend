package com.music.userMS.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.music.userMS.dto.ArtistRequestDTO;
import com.music.userMS.dto.ArtistResponseDTO;
import com.music.userMS.dto.UserDetailsResponseDTO;
import com.music.userMS.dto.UserRequestDTO;
import com.music.userMS.dto.UserResponseDTO;
import com.music.userMS.exception.EmailAlreadyUsedException;
import com.music.userMS.exception.NotFoundException;
import com.music.userMS.model.Role;
import com.music.userMS.model.Roles;
import com.music.userMS.model.User;
import com.music.userMS.repository.RoleRepository;
import com.music.userMS.repository.UserRepository;

import reactor.core.publisher.Mono;

@Service(value = "userService")
public class UserService {
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private WebClient.Builder webClientBuilder;
	
	@Transactional(readOnly = true)
	public List<UserResponseDTO> findFollowedUsersById(Integer id) throws NotFoundException {
		Optional<User> optional = repository.findById(id);
		if (optional.isPresent()) {
			return repository.findFollowedUsersById(id)
					.stream()
					.map( user -> {
						return new UserResponseDTO(user);
					}).toList();
		} else {
			throw new NotFoundException("User", id);
		}
	}
	
	@Transactional(readOnly = true)
	public List<UserResponseDTO> findFollowersById(Integer id) throws NotFoundException {
		Optional<User> optional = repository.findById(id);
		if (optional.isPresent()) {
			return repository.findFollowersById(id)
					.stream()
					.map( user -> {
						return new UserResponseDTO(user);
					}).toList();
		} else {
			throw new NotFoundException("User", id);
		}
	}

	@Transactional(readOnly = true)
	public List<UserResponseDTO> findAll() {
		return repository.findAllNotDeletedUsers()
				.stream()
				.map( UserResponseDTO::new )
				.toList();
	}
	
	@Transactional(readOnly = true)
	public List<UserResponseDTO> findAllDeletedUsers() {
		return repository.findAllDeletedUsers()
				.stream()
				.map( UserResponseDTO::new )
				.toList();
	}
	
	@Transactional(readOnly = true)
	public UserResponseDTO findById(Integer id) throws NotFoundException {
		Optional<User> optional = repository.findById(id);
		if (!optional.isPresent() || optional.get().getIsDeleted()) {
			throw new NotFoundException("User", id);
		}
		
		User user = optional.get();
		return new UserResponseDTO(user);
	}
	
	@Transactional(readOnly = true)
	public UserResponseDTO findByIdEvenDeleted(Integer id) throws NotFoundException {
		Optional<User> optional = repository.findById(id);
		if (!optional.isPresent()) {
			throw new NotFoundException("User", id);
		}
		
		User user = optional.get();
		return new UserResponseDTO(user);
	}
	
	@Transactional(readOnly = true)
	public UserDetailsResponseDTO findByEmail(String email) throws NotFoundException {
		Optional<User> optional = repository.findByEmailAndNotDeleted(email);
		if (optional.isPresent()) {
			return new UserDetailsResponseDTO(optional.get());
		} else {
			throw new NotFoundException("User", email);
		}
	}
	
	@Transactional // not allowed to save an user with the same email, even if the user with that email is deleted
	public UserResponseDTO saveUser(UserRequestDTO request) throws EmailAlreadyUsedException, NotFoundException {
		Optional<User> optional = repository.findByEmail(request.getEmail());
		Optional<Role> roleOptional = roleRepository.findByName(Roles.USER);
		
		if (!roleOptional.isPresent()) {
			throw new NotFoundException("Role", Roles.USER.toLowerCase());
		}
		if (optional.isPresent()) {
			throw new EmailAlreadyUsedException(request.getEmail());
		}
		
		Role role = roleOptional.get();
		User user = new User(request, role);
		
		return new UserResponseDTO(repository.save(user));
	}
	
	@Transactional // not allowed to save an user with the same email, even if the user with that email is deleted
	public UserResponseDTO saveArtistUser(UserRequestDTO request) throws EmailAlreadyUsedException, NotFoundException {
		Optional<User> optional = repository.findByEmail(request.getEmail());
		Optional<Role> roleOptional = roleRepository.findByName(Roles.ARTIST);
		
		if (!roleOptional.isPresent()) {
			throw new NotFoundException("Role", Roles.ARTIST.toLowerCase());
		}
		if (optional.isPresent()) {
			throw new EmailAlreadyUsedException(request.getEmail());
		}
		
		Role role = roleOptional.get();
		User user = new User(request, role);
		
		UserResponseDTO userDTO = new UserResponseDTO(repository.save(user));
		
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
	}
	
	@Transactional
	public UserResponseDTO updateUser(Integer id, UserRequestDTO request) throws NotFoundException, EmailAlreadyUsedException {
		Optional<User> optional = repository.findByEmail(request.getEmail());
		if (optional.isPresent() && optional.get().getId() != id) {
			throw new EmailAlreadyUsedException(request.getEmail());
		}
		
		optional = repository.findById(id);
		if (!optional.isPresent() || optional.get().getIsDeleted()) {
			throw new NotFoundException("User", id);
		}
		
		User user = optional.get();
		user.setUsername(request.getUsername());
		user.setEmail(request.getEmail());
		user.setAddress(request.getAddress());
		user.setPassword(request.getPassword());
		
		return new UserResponseDTO(repository.save(user));
	}
	
	@Transactional
	public UserResponseDTO deleteUser(Integer id) throws NotFoundException {
		Optional<User> optional = repository.findById(id);
		if (!optional.isPresent() || optional.get().getIsDeleted()) {
			throw new NotFoundException("User", id);
		}
		
		User user = optional.get();
		user.setIsDeleted(true);
		
		try {
			webClientBuilder.build()
				.delete()
				.uri("http://localhost:8002/api/artist/user/" + id)
				.retrieve()
				.bodyToMono(ArtistResponseDTO.class)
				.block();	
		} catch (Exception e) {
			System.err.println(String.format("Artist with userId %s doesn't exist", id));
//			e.getStackTrace();
		}
		
		return new UserResponseDTO(repository.save(user));
	}	
}
