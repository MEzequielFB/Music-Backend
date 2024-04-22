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

import com.music.musicMS.dto.SongRequestDTO;
import com.music.musicMS.dto.SongResponseDTO;
import com.music.musicMS.exception.ArtistNotInSongException;
import com.music.musicMS.exception.AuthorizationException;
import com.music.musicMS.exception.NameAlreadyUsedException;
import com.music.musicMS.exception.NotFoundException;
import com.music.musicMS.exception.SomeEntityDoesNotExistException;
import com.music.musicMS.model.Roles;
import com.music.musicMS.service.SongService;

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
@RequestMapping("/api/song")
@Tag(name = "Song Controller", description = "<p>Used for song management: create/delete/return/listen songs, update songs attributes</p>")
public class SongController {

	@Autowired
	private SongService service;
	
	//By name, genre and release year
	@Operation(summary = "Find songs by name, genre  and release year", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li><li>USER</li><li>ARTIST</li></ul> ",
			parameters = {
				@Parameter(name = "data", description = "Song name, genre or release year", required = false)
			})
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("/search")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "', '" + Roles.ARTIST + "')")
	public ResponseEntity<List<SongResponseDTO>> findAllByFilters(@RequestParam(required = false) List<String> data) {
		return ResponseEntity.ok(service.findAllByFilters(data));
	}
	
	@Operation(summary = "Find all songs", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li><li>USER</li><li>ARTIST</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "', '" + Roles.ARTIST + "')")
	public ResponseEntity<List<SongResponseDTO>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}
	
	@Operation(summary = "Find song by id", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li><li>USER</li><li>ARTIST</li></ul> ",
			parameters = {
				@Parameter(name = "id", description = "Song id", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Song found", content = {
			@Content(schema = @Schema(implementation = SongResponseDTO.class))
		}),
		@ApiResponse(responseCode = "404", description = "Song not found", content = {
			@Content(schema = @Schema(example = "The entity Song with id '1' doesn't exist"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "', '" + Roles.ARTIST + "')")
	public ResponseEntity<SongResponseDTO> findById(@PathVariable Integer id) throws NotFoundException {
		return ResponseEntity.ok(service.findById(id));
	}
	
	@Operation(summary = "Save song. The logged artist should be on the list of artists", description = "<p>Required roles:</p> <ul><li>ARTIST</li></ul> ",
			parameters = {
				@Parameter(name = "Authorization", description = "Authentication token provided when login or registering", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "Song saved", content = {
			@Content(schema = @Schema(implementation = SongResponseDTO.class))
		}),
		@ApiResponse(responseCode = "404", description = "Album not found if the albumId field is not null OR some artist/genre hasn't been found by the entered ids", content = {
			@Content(schema = @Schema(example = "The entity Album with id '1' doesn't exist OR some of the artists/genres passed as an argument doesn't exist"))
		}),
		@ApiResponse(responseCode = "401", description = "No authenticated user", content = {
			@Content(schema = @Schema(example = "No authenticated user available"))
		}),
		@ApiResponse(responseCode = "400", description = "Song with the entered name already exist for the logged artist OR the logged artist is not part of the song", content = {
			@Content(schema = @Schema(example = "A Song with the name 'song1' already exists OR Can't save the song because the logged artist is not part of the song"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@PostMapping("")
	@PreAuthorize("hasAuthority('" + Roles.ARTIST + "')")
	public ResponseEntity<SongResponseDTO> saveSong(@RequestBody @Valid SongRequestDTO request, @RequestHeader("Authorization") String token) throws NameAlreadyUsedException, SomeEntityDoesNotExistException, NotFoundException, AuthorizationException, ArtistNotInSongException {
		return new ResponseEntity<>(service.saveSong(request, token), HttpStatus.CREATED);
	}
	
	@Operation(summary = "Update song. The logged artist should be on the list of artists", description = "<p>Required roles:</p> <ul><li>ARTIST</li></ul> ",
			parameters = {
				@Parameter(name = "id", description = "Song id", required = true),
				@Parameter(name = "Authorization", description = "Authentication token provided when login or registering", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Song updated", content = {
			@Content(schema = @Schema(implementation = SongResponseDTO.class))
		}),
		@ApiResponse(responseCode = "404", description = "Song not found OR Album not found if the albumId field is not null OR some artist/genre hasn't been found by the entered ids", content = {
			@Content(schema = @Schema(example = "The entity Album/Song with id '1' doesn't exist OR some of the artists/genres passed as an argument doesn't exist"))
		}),
		@ApiResponse(responseCode = "401", description = "No authenticated user", content = {
			@Content(schema = @Schema(example = "No authenticated user available"))
		}),
		@ApiResponse(responseCode = "400", description = "Song with the entered name already exist for the logged artist OR the logged artist is not part of the song", content = {
			@Content(schema = @Schema(example = "A Song with the name 'song1' already exists OR Can't save the song because the logged artist is not part of the song"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('" + Roles.ARTIST + "')")
	public ResponseEntity<SongResponseDTO> updateSong(@PathVariable Integer id, @RequestBody @Valid SongRequestDTO request, @RequestHeader("Authorization") String token) throws NotFoundException, SomeEntityDoesNotExistException, AuthorizationException, ArtistNotInSongException {
		return ResponseEntity.ok(service.updateSong(id, request, token));
	}
	
	@Operation(summary = "Listen to a song", description = "<p>Required roles:</p> <ul><li>USER</li><li>ARTIST</li></ul> ",
			parameters = {
				@Parameter(name = "id", description = "Song id", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Song listened", content = {
			@Content(schema = @Schema(implementation = SongResponseDTO.class))
		}),
		@ApiResponse(responseCode = "404", description = "Song not found", content = {
			@Content(schema = @Schema(example = "The entity Song with id '1' doesn't exist"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@PutMapping("/{id}/listen")
	@PreAuthorize("hasAnyAuthority('" + Roles.ARTIST + "', '" + Roles.USER + "')")
	public ResponseEntity<SongResponseDTO> listenSong(@PathVariable Integer id) throws NotFoundException {
		return ResponseEntity.ok(service.listenSong(id));
	}
	
	@Operation(summary = "Delete song", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li><li>ARTIST</li></ul> ",
			parameters = {
				@Parameter(name = "id", description = "Song id", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Song deleted", content = {
			@Content(schema = @Schema(implementation = SongResponseDTO.class))
		}),
		@ApiResponse(responseCode = "404", description = "Song not found", content = {
			@Content(schema = @Schema(example = "The entity Song with id '1' doesn't exist"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.ARTIST + "')")
	public ResponseEntity<SongResponseDTO> deleteSong(@PathVariable Integer id) throws NotFoundException {
		return ResponseEntity.ok(service.deleteSong(id));
	}
}
