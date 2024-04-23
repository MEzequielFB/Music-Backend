package com.music.musicMS.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.music.musicMS.dto.ArtistRequestDTO;
import com.music.musicMS.dto.ArtistResponseDTO;
import com.music.musicMS.dto.NameRequestDTO;
import com.music.musicMS.exception.AuthorizationException;
import com.music.musicMS.exception.NameAlreadyUsedException;
import com.music.musicMS.exception.NotFoundException;
import com.music.musicMS.exception.PermissionsException;
import com.music.musicMS.model.Roles;
import com.music.musicMS.service.ArtistService;

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

@RestController
@RequestMapping("/api/artist")
@Tag(name = "Artist Controller", description = "<p>Used for artist management: return artists. creation/deletetion/update methods are called from the User Controller</p>")
public class ArtistController {

	@Autowired
	private ArtistService service;
	
	@Operation(summary = "Find all artists", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li><li>USER</li><li>ARTIST</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "', '" + Roles.ARTIST + "')")
	public ResponseEntity<List<ArtistResponseDTO>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}
	
	@Operation(summary = "Find all artists by name", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li><li>USER</li><li>ARTIST</li></ul> ",
			parameters = {
				@Parameter(name = "name", description = "Artist name", required = false)
			})
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("/search")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "', '" + Roles.ARTIST + "')")
	public ResponseEntity<List<ArtistResponseDTO>> findAllByName(@RequestParam(required = false) String name) {
		return ResponseEntity.ok(service.findAllByName(name));
	}
	
	@Operation(summary = "Find all deleted artists", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("/deleted")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "')")
	public ResponseEntity<List<ArtistResponseDTO>> findAllDeletedArtists() {
		return ResponseEntity.ok(service.findAllDeleted());
	}
	
	@Operation(summary = "Find artist by id", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li><li>USER</li><li>ARTIST</li></ul> ",
			parameters = {
				@Parameter(name = "id", description = "Artist id", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Artist found", content = {
			@Content(schema = @Schema(implementation = ArtistResponseDTO.class))
		}),
		@ApiResponse(responseCode = "404", description = "Artist not found", content = {
			@Content(schema = @Schema(example = "The entity Artist with id '1' doesn't exist"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "', '" + Roles.ARTIST + "')")
	public ResponseEntity<ArtistResponseDTO> findById(@PathVariable Integer id) throws NotFoundException {
		return ResponseEntity.ok(service.findById(id));
	}
	
	@Operation(summary = "Find artists by ids", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li><li>USER</li><li>ARTIST</li></ul> ",
			parameters = {
				@Parameter(name = "ids", description = "Artists ids", required = true)
			})
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("/allByIds")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "', '" + Roles.ARTIST + "')")
	public ResponseEntity<List<ArtistResponseDTO>> findAllByIds(@RequestParam List<Integer> ids) {
		return ResponseEntity.ok(service.findAllByids(ids));
	}

	@Operation(summary = "Save an artist", description = "<p>Permit all roles</p>")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "Artist saved", content = {
			@Content(schema = @Schema(implementation = ArtistResponseDTO.class))
		}),
		@ApiResponse(responseCode = "400", description = "Artist with the entered name already exists", content = {
			@Content(schema = @Schema(example = "An Artist with the name 'artist1' already exists"))
		})
	})
	@Hidden
	@PostMapping("") // permit all
	public ResponseEntity<ArtistResponseDTO> saveArtist(@RequestBody @Valid ArtistRequestDTO request) throws NameAlreadyUsedException {
		return new ResponseEntity<>(service.saveArtist(request), HttpStatus.CREATED);
	}
	
	@Operation(summary = "Update an artist", description = "<p>Required roles:</p> <ul><li>ARTIST</li></ul> ",
			parameters = {
				@Parameter(name = "id", description = "Artist id", required = true),
				@Parameter(name = "Authorization", description = "Authentication token provided when login in or registering", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Artist updated", content = {
			@Content(schema = @Schema(implementation = ArtistResponseDTO.class))
		}),
		@ApiResponse(responseCode = "404", description = "Artist not found", content = {
			@Content(schema = @Schema(example = "The entity Artist with id '1' doesn't exist"))
		}),
		@ApiResponse(responseCode = "403", description = "The logged artist doesn't have the permission to update another artist", content = {
			@Content(schema = @Schema(example = "The user with id '1' doesn't have permissions to do the current action"))
		}),
		@ApiResponse(responseCode = "401", description = "No authenticated user", content = {
			@Content(schema = @Schema(example = "No authenticated user available"))
		}),
		@ApiResponse(responseCode = "400", description = "Artist with the entered name already exists", content = {
			@Content(schema = @Schema(example = "An Artist with the name 'artist1' already exists"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@Hidden
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('" + Roles.ARTIST + "')")
	public ResponseEntity<ArtistResponseDTO> updateArtist(@PathVariable Integer id, @RequestBody @Valid NameRequestDTO request, @RequestHeader("Authorization") String token) throws NotFoundException, AuthorizationException, PermissionsException, NameAlreadyUsedException {
		return ResponseEntity.ok(service.updateArtist(id, request, token));
	}
	
	@Operation(summary = "Update an artist by userId", description = "<p>Required roles:</p> <ul><li>ARTIST</li></ul> ",
			parameters = {
				@Parameter(name = "userId", description = "User id", required = true),
				@Parameter(name = "Authorization", description = "Authentication token provided when login in or registering", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Artist updated", content = {
			@Content(schema = @Schema(implementation = ArtistResponseDTO.class))
		}),
		@ApiResponse(responseCode = "404", description = "Artist not found", content = {
			@Content(schema = @Schema(example = "The entity Artist with id '1' doesn't exist"))
		}),
		@ApiResponse(responseCode = "403", description = "The logged artist doesn't have the permission to update another artist", content = {
			@Content(schema = @Schema(example = "The user with id '1' doesn't have permissions to do the current action"))
		}),
		@ApiResponse(responseCode = "401", description = "No authenticated user", content = {
			@Content(schema = @Schema(example = "No authenticated user available"))
		}),
		@ApiResponse(responseCode = "400", description = "Artist with the entered name already exists", content = {
			@Content(schema = @Schema(example = "An Artist with the name 'artist1' already exists"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@Hidden
	@PutMapping("/user/{userId}")
	@PreAuthorize("hasAuthority('" + Roles.ARTIST + "')")
	public ResponseEntity<ArtistResponseDTO> updateArtistByUserId(@PathVariable Integer userId, @RequestBody @Valid NameRequestDTO request, @RequestHeader("Authorization") String token) throws NotFoundException, AuthorizationException, PermissionsException, NameAlreadyUsedException {
		return ResponseEntity.ok(service.updateArtistByUserId(userId, request, token));
	}
	
	@Operation(summary = "Delete an artist", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li></ul> ",
			parameters = {
				@Parameter(name = "id", description = "Artist id", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Artist deleted", content = {
			@Content(schema = @Schema(implementation = ArtistResponseDTO.class))
		}),
		@ApiResponse(responseCode = "404", description = "Artist not found", content = {
			@Content(schema = @Schema(example = "The entity Artist with id '1' doesn't exist"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@Hidden
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "')")
	public ResponseEntity<ArtistResponseDTO> deleteArtist(@PathVariable Integer id) throws NotFoundException {
		return ResponseEntity.ok(service.deleteArtist(id));
	}
	
	@Operation(summary = "Delete an artist by userId", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li></ul> ",
			parameters = {
				@Parameter(name = "userId", description = "User id", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Artist deleted", content = {
			@Content(schema = @Schema(implementation = ArtistResponseDTO.class))
		}),
		@ApiResponse(responseCode = "404", description = "Artist not found", content = {
			@Content(schema = @Schema(example = "The entity User with userId '1' doesn't exist"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@Hidden
	@DeleteMapping("/user/{userId}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "')")
	public ResponseEntity<ArtistResponseDTO> deleteArtistByUserId(@PathVariable Integer userId) throws NotFoundException {
		return ResponseEntity.ok(service.deleteArtistByUserId(userId));
	}
}
