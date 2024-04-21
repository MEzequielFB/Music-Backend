package com.music.userMS.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.music.userMS.dto.FollowRequestDTO;
import com.music.userMS.dto.RoleUpdateRequestDTO;
import com.music.userMS.dto.UserDetailsResponseDTO;
import com.music.userMS.dto.UserFollowerResponseDTO;
import com.music.userMS.dto.UserRequestDTO;
import com.music.userMS.dto.UserResponseDTO;
import com.music.userMS.exception.AuthorizationException;
import com.music.userMS.exception.EmailAlreadyUsedException;
import com.music.userMS.exception.InvalidRoleException;
import com.music.userMS.exception.NotFoundException;
import com.music.userMS.model.Roles;
import com.music.userMS.service.UserFollowerService;
import com.music.userMS.service.UserService;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController(value = "userController")
@RequestMapping("/api/user")
@Tag(name = "User Controller", description = "<p>Used for user management: create/logic delete/return users, update users attributes</p>")
public class UserController {
	@Autowired
	private UserService service;
	
	@Autowired
	private UserFollowerService userFollowerService;
	
//	@GetMapping("/{id}/followedUsers")
//	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.USER + "', '" + Roles.ARTIST + "')")
//	public ResponseEntity<List<UserResponseDTO>> findFollowedUsersById(@PathVariable Integer id) throws NotFoundException {
//		return ResponseEntity.ok(service.findFollowedUsersById(id));
//	}
//	
//	@GetMapping("/{id}/followers")
//	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.USER + "', '" + Roles.ARTIST + "')")
//	public ResponseEntity<List<UserResponseDTO>> findFollowersById(@PathVariable Integer id) throws NotFoundException {
//		return ResponseEntity.ok(service.findFollowersById(id)); 
//	}
//	
//	@PostMapping("/follow")
//	@PreAuthorize("hasAnyAuthority('" + Roles.USER + "', '" + Roles.ARTIST + "')")
//	public ResponseEntity<UserFollowerResponseDTO> followUser(@RequestBody @Valid FollowRequestDTO request) throws NotFoundException {
//		return new ResponseEntity<>(userFollowerService.followUser(request), HttpStatus.CREATED);
//	}
	
	@Operation(summary = "Find all users", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li><li>USER</li><li>ARTIST</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "', '" + Roles.ARTIST + "')")
	public ResponseEntity<List<UserResponseDTO>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}
	
	@Operation(summary = "Find all deleted users", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("/deleted")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.DELIVERY + "')")
	public ResponseEntity<List<UserResponseDTO>> findAllDeletedUsers() {
		return ResponseEntity.ok(service.findAllDeletedUsers());
	}
	
	@Operation(summary = "Find user by id", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li><li>USER</li><li>ARTIST</li></ul> ",
			parameters = {
				@Parameter(name = "id", description = "User id", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode =  "200", description = "User found", content = {
			@Content(schema = @Schema(implementation = UserResponseDTO.class))
		}),
		@ApiResponse(responseCode =  "404", description = "User not found", content = {
			@Content(schema = @Schema(example = "The entity User with id '1' doesn't exist"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "', '" + Roles.ARTIST + "')")
	public ResponseEntity<UserResponseDTO> findById(@PathVariable Integer id) throws NotFoundException {
		return ResponseEntity.ok(service.findById(id));
		
	}
	
	@Operation(summary = "Find user by id even if is deleted", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li></ul> ",
			parameters = {
				@Parameter(name = "id", description = "User id", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode =  "200", description = "User found", content = {
			@Content(schema = @Schema(implementation = UserResponseDTO.class))
		}),
		@ApiResponse(responseCode =  "404", description = "User not found", content = {
			@Content(schema = @Schema(example = "The entity User with id '1' doesn't exist"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("/{id}/evenDeleted")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.DELIVERY + "')")
	public ResponseEntity<UserResponseDTO> findByIdEvenDeleted(@PathVariable Integer id) throws NotFoundException {
		return ResponseEntity.ok(service.findByIdEvenDeleted(id));
	}
	
	@Operation(summary = "Find user by email", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li><li>USER</li><li>ARTIST</li><li>DELIVERY</li></ul>",
			parameters = {
				@Parameter(name = "email", description = "User email", required = true)
			})
	@ApiResponses(value = {
			@ApiResponse(responseCode =  "200", description = "User found", content = {
				@Content(schema = @Schema(implementation = UserDetailsResponseDTO.class))
			}),
			@ApiResponse(responseCode =  "404", description = "User not found", content = {
				@Content(schema = @Schema(example = "The entity User with the email 'user1@gmail.com' doesn't exist"))
			})
		})
	@Hidden
	@GetMapping("/email/{email}") // Permit all
	public ResponseEntity<UserDetailsResponseDTO> findByEmail(@PathVariable String email) throws NotFoundException {
		return ResponseEntity.ok(service.findByEmail(email));
	}
	
	@Operation(summary = "Save user", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li><li>USER</li><li>ARTIST</li><li>DELIVERY</li></ul>")
	@ApiResponses(value = {
		@ApiResponse(responseCode =  "201", description = "User saved", content = {
			@Content(schema = @Schema(implementation = UserResponseDTO.class))
		}),
		@ApiResponse(responseCode =  "404", description = "Role not found", content = {
			@Content(schema = @Schema(example = "The entity Role with name 'USER' doesn't exist"))
		}),
		@ApiResponse(responseCode =  "400", description = "Entered email already in use", content = {
			@Content(schema = @Schema(example = "The email 'user1@gmail.com' is already in use"))
		})
	})
	@Hidden
	@PostMapping("") // Permit all
	public ResponseEntity<UserResponseDTO> saveUser(@RequestBody @Valid UserRequestDTO request) throws EmailAlreadyUsedException, NotFoundException {
		return new ResponseEntity<>(service.saveUser(request), HttpStatus.CREATED);
	}
	
	@Operation(summary = "Save artist user", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li><li>USER</li><li>ARTIST</li><li>DELIVERY</li></ul>")
	@ApiResponses(value = {
		@ApiResponse(responseCode =  "201", description = "Artist saved", content = {
			@Content(schema = @Schema(implementation = UserResponseDTO.class))
		}),
		@ApiResponse(responseCode =  "404", description = "Role not found", content = {
			@Content(schema = @Schema(example = "The entity Role with name 'ARTIST' doesn't exist"))
		}),
		@ApiResponse(responseCode =  "400", description = "Entered email already in use OR the entered username is already in use", content = {
			@Content(schema = @Schema(example = "The email 'user1@gmail.com' is already in use OR an artists with the username 'artist1' already exists"))
		})
	})
	@Hidden
	@PostMapping("/artist") // Permit all
	public ResponseEntity<UserResponseDTO> saveArtistUser(@RequestBody @Valid UserRequestDTO request) throws EmailAlreadyUsedException, NotFoundException {
		return new ResponseEntity<>(service.saveArtistUser(request), HttpStatus.CREATED);
	}
	
	@Operation(summary = "The logged user update his attributes", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li><li>USER</li><li>ARTIST</li><li>DELIVERY</li></ul> ",
			parameters = {
				@Parameter(name = "Authorization", description = "Authentication token provided when login or register", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode =  "200", description = "User updated", content = {
			@Content(schema = @Schema(implementation = UserResponseDTO.class))
		}),
		@ApiResponse(responseCode =  "404", description = "User not found", content = {
			@Content(schema = @Schema(example = "The entity User with id '1' doesn't exist"))
		}),
		@ApiResponse(responseCode =  "400", description = "Entered email already in use OR the entered username is already in use", content = {
			@Content(schema = @Schema(example = "The email 'user1@gmail.com' is already in use OR an artists with the username 'artist1' already exists"))
		}),
		@ApiResponse(responseCode =  "401", description = "No authenticated user", content = {
			@Content(schema = @Schema(example = "No authenticated user available"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@PutMapping("")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "', '" + Roles.ARTIST + "', '" + Roles.DELIVERY + "')")
	public ResponseEntity<UserResponseDTO> updateUser(@RequestBody @Valid UserRequestDTO request, @RequestHeader("Authorization") String token) throws NotFoundException, EmailAlreadyUsedException, AuthorizationException {
		return ResponseEntity.ok(service.updateUser(request, token));
	}
	
	@Operation(summary = "As a super admin change the role of users (user, admin or delivery)", description = "<p>Required roles:</p> <ul><li>SUPER_ADMIN</li></ul>",
			parameters = {
				@Parameter(name = "id", description = "User id", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode =  "200", description = "User role updated", content = {
			@Content(schema = @Schema(implementation = UserResponseDTO.class))
		}),
		@ApiResponse(responseCode =  "404", description = "User or Role not found", content = {
			@Content(schema = @Schema(example = "The entity User with id '1' doesn't exist OR the entity Role with name 'USER' doesn't exist"))
		}),
		@ApiResponse(responseCode =  "400", description = "Entered role is not valid or the permissions from the user can't be change", content = {
			@Content(schema = @Schema(example = "Cannot change permissions of user with the role 'SUPER_ADMIN'"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@PutMapping("/role/{id}")
	@PreAuthorize("hasAnyAuthority('" + Roles.SUPER_ADMIN + "')")
	public ResponseEntity<UserResponseDTO> updateUserRole(@PathVariable Integer id, @RequestBody @Valid RoleUpdateRequestDTO request) throws NotFoundException, InvalidRoleException {
		return ResponseEntity.ok(service.updateUserRole(id, request));
	}
	
	@Operation(summary = "Delete an user", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li></ul>",
			parameters = {
				@Parameter(name = "id", description = "User id", required = true),
				@Parameter(name = "Authorization", description = "Authentication token provided when login or register", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode =  "200", description = "User deleted", content = {
			@Content(schema = @Schema(implementation = UserResponseDTO.class))
		}),
		@ApiResponse(responseCode =  "404", description = "User not found", content = {
			@Content(schema = @Schema(example = "The entity User with id '1' doesn't exist"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "')")
	public ResponseEntity<UserResponseDTO> deleteUser(@PathVariable Integer id, @RequestHeader("Authorization") String token) throws NotFoundException {
		return ResponseEntity.ok(service.deleteUser(id, token));
	}
}
