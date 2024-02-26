package com.music.userMS.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerErrorException;

import com.music.userMS.dto.ArtistRequestDTO;
import com.music.userMS.dto.ArtistResponseDTO;
import com.music.userMS.dto.NameRequestDTO;
import com.music.userMS.dto.RoleUpdateRequestDTO;
import com.music.userMS.dto.UserDetailsResponseDTO;
import com.music.userMS.dto.UserRequestDTO;
import com.music.userMS.dto.UserResponseDTO;
import com.music.userMS.exception.AuthorizationException;
import com.music.userMS.exception.EmailAlreadyUsedException;
import com.music.userMS.exception.InvalidRoleException;
import com.music.userMS.exception.NotFoundException;
import com.music.userMS.model.Role;
import com.music.userMS.model.Roles;
import com.music.userMS.model.User;
import com.music.userMS.repository.RoleRepository;
import com.music.userMS.repository.UserRepository;

@Service(value = "userService")
public class UserService {
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private WebClient.Builder webClientBuilder;
	
	@Value("${app.api.domain}")
	private String domain;
	
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
		
		System.err.println(user);
		System.err.println(userDTO);
		
		try {
			webClientBuilder.build()
				.post()
				.uri(String.format("%s:8002/api/artist", this.domain))
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(new ArtistRequestDTO(user.getUsername(), user.getId()))
				.retrieve()
				.bodyToMono(ArtistResponseDTO.class)
				.block();	
		} catch (Exception e) {
			System.err.println(e);
			throw new ServerErrorException("Server not respond or the username is already in use", e);
		}
		
		return userDTO;
	}
	
	@Transactional
	public UserResponseDTO updateUser(UserRequestDTO request, String token) throws NotFoundException, EmailAlreadyUsedException, AuthorizationException {
		Integer loggedUserId = null;
		try {
			loggedUserId = webClientBuilder.build()
					.get()
					.uri(String.format("%s:8004/api/auth/id", this.domain))
					.header("Authorization", token)
					.retrieve()
					.bodyToMono(Integer.class)
					.block();
		} catch (Exception e) {
			System.err.println(e);
			throw new AuthorizationException();
		}
		
		Optional<User> optional = repository.findByEmail(request.getEmail());
		if (optional.isPresent() && optional.get().getId() != loggedUserId) {
			throw new EmailAlreadyUsedException(request.getEmail());
		}
		
		optional = repository.findById(loggedUserId);
		if (!optional.isPresent() || optional.get().getIsDeleted()) {
			throw new NotFoundException("User", loggedUserId);
		}
		
		User user = optional.get();
		String encodedPassword = passwordEncoder.encode(request.getPassword());
		
		if (!user.getUsername().equals(request.getUsername())) {
			try {
				webClientBuilder.build()
					.put()
					.uri(String.format("%s:8002/api/artist/user/%s", this.domain, loggedUserId))
					.header("Authorization", token)
					.contentType(MediaType.APPLICATION_JSON)
					.bodyValue(new NameRequestDTO(request.getUsername()))
					.retrieve()
					.bodyToMono(ArtistResponseDTO.class)
					.block();
			} catch (Exception e) {
				System.err.println(String.format("Artist with userId %s doesn't exist", loggedUserId));
			}
		}
		
		user.setUsername(request.getUsername());
		user.setEmail(request.getEmail());
		user.setAddress(request.getAddress());
		user.setPassword(encodedPassword);
		
		return new UserResponseDTO(repository.save(user));
	}
	
	@Transactional
	public UserResponseDTO updateUserRole(Integer id, RoleUpdateRequestDTO request) throws NotFoundException, InvalidRoleException {
		Optional<User> optional = repository.findById(id);
		Optional<Role> requestOptional = roleRepository.findByName(request.getRole());
		Optional<Role> userRoleOptional = roleRepository.findByName(Roles.USER);
		Optional<Role> adminRoleOptional = roleRepository.findByName(Roles.ADMIN);
		Optional<Role> deliveryRoleOptional = roleRepository.findByName(Roles.DELIVERY);
		
		if (!optional.isPresent()) {
			throw new NotFoundException("User", id);
		}
		
		if (!requestOptional.isPresent()) {
			throw new NotFoundException("Role", request.getRole());
		}
		
		if (!userRoleOptional.isPresent()) {
			throw new NotFoundException("Role", Roles.USER);
		}
		
		if (!adminRoleOptional.isPresent()) {
			throw new NotFoundException("Role", Roles.ADMIN);
		}
		
		User user = optional.get();
		Role requestRole = requestOptional.get();
		Role userRole = userRoleOptional.get();
		Role adminRole = adminRoleOptional.get();
		Role deliveryRole = deliveryRoleOptional.get();
		
		if ((!user.getRole().equals(userRole) && !user.getRole().equals(adminRole) && !user.getRole().equals(deliveryRole)) || (!requestRole.equals(userRole) && !requestRole.equals(adminRole) && !requestRole.equals(deliveryRole))) {
			throw new InvalidRoleException(user.getRole().getName());
		}
		
		user.setRole(requestRole);
		return new UserResponseDTO(repository.save(user));
	}
	
	@Transactional
	public UserResponseDTO deleteUser(Integer id, String token) throws NotFoundException {
		Optional<User> optional = repository.findById(id);
		if (!optional.isPresent() || optional.get().getIsDeleted()) {
			throw new NotFoundException("User", id);
		}
		
		User user = optional.get();
		user.setIsDeleted(true);
		
		try {
			webClientBuilder.build()
				.delete()
				.uri(String.format("%s:8002/api/artist/user/%s", this.domain, id))
				.header("Authorization", token)
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
