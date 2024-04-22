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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.music.musicMS.dto.GenreRequestDTO;
import com.music.musicMS.dto.GenreResponseDTO;
import com.music.musicMS.exception.NameAlreadyUsedException;
import com.music.musicMS.exception.NotFoundException;
import com.music.musicMS.model.Roles;
import com.music.musicMS.service.GenreService;

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
@RequestMapping("/api/genre")
@Tag(name = "Genre Controller", description = "<p>Used for genre management: create/delete/return genres, update genres attributes</p>")
public class GenreController {

	@Autowired
	private GenreService service;
	
	@Operation(summary = "Find all genres", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li><li>USER</li><li>ARTIST</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "', '" + Roles.ARTIST + "')")
	public ResponseEntity<List<GenreResponseDTO>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}
	
	@Operation(summary = "Find genre by id", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li><li>USER</li><li>ARTIST</li></ul> ",
			parameters = {
				@Parameter(name = "id", description = "Genre id", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Genre found", content = {
			@Content(schema = @Schema(implementation = GenreResponseDTO.class))
		}),
		@ApiResponse(responseCode = "404", description = "Genre not found", content = {
			@Content(schema = @Schema(example = "The entity Genre with id '1' doesn't exist"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "', '" + Roles.ARTIST + "')")
	public ResponseEntity<GenreResponseDTO> findById(@PathVariable Integer id) throws NotFoundException {
		return ResponseEntity.ok(service.findById(id));
	}
	
	@Operation(summary = "Save genre", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li></ul> ")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "Genre saved", content = {
			@Content(schema = @Schema(implementation = GenreResponseDTO.class))
		}),
		@ApiResponse(responseCode = "400", description = "Entered name for genre is already in use", content = {
			@Content(schema = @Schema(example = "A Genre with the name 'genre' already exists"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@PostMapping("")
	@PreAuthorize( "hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "')" )
	public ResponseEntity<GenreResponseDTO> saveGenre(@RequestBody @Valid GenreRequestDTO request) throws NameAlreadyUsedException {
		return new ResponseEntity<>(service.saveGenre(request), HttpStatus.CREATED);
	}

	@Operation(summary = "Update genre", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li></ul> ",
			parameters = {
				@Parameter(name = "id", description = "Genre id", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Genre updated", content = {
			@Content(schema = @Schema(implementation = GenreResponseDTO.class))
		}),
		@ApiResponse(responseCode = "404", description = "Genre not found", content = {
			@Content(schema = @Schema(example = "The entity Genre with id '1' doesn't exist"))
		}),
		@ApiResponse(responseCode = "400", description = "Entered name already in use", content = {
			@Content(schema = @Schema(example = "A Genre with the name 'genre1' already exists"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@PutMapping("/{id}")
	@PreAuthorize( "hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "')" )
	public ResponseEntity<GenreResponseDTO> updateGenre(@PathVariable Integer id, @RequestBody @Valid GenreRequestDTO request) throws NotFoundException, NameAlreadyUsedException {
		return ResponseEntity.ok(service.updateGenre(id, request));
	}
	
	@Operation(summary = "Delete genre", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li></ul> ",
			parameters = {
				@Parameter(name = "id", description = "Genre id", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Genre deleted", content = {
			@Content(schema = @Schema(implementation = GenreResponseDTO.class))
		}),
		@ApiResponse(responseCode = "404", description = "Genre not found", content = {
			@Content(schema = @Schema(example = "The entity Genre with id '1' doesn't exist"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@DeleteMapping("/{id}")
	@PreAuthorize( "hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "')" )
	public ResponseEntity<GenreResponseDTO> deleteGenre(@PathVariable Integer id) throws NotFoundException {
		return ResponseEntity.ok(service.deleteGenre(id));
	}
}
