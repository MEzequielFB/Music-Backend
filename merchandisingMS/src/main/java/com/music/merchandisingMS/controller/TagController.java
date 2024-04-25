package com.music.merchandisingMS.controller;

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

import com.music.merchandisingMS.dto.TagRequestDTO;
import com.music.merchandisingMS.dto.TagResponseDTO;
import com.music.merchandisingMS.exception.NameAlreadyUsedException;
import com.music.merchandisingMS.exception.NoTagsException;
import com.music.merchandisingMS.exception.NotFoundException;
import com.music.merchandisingMS.model.Roles;
import com.music.merchandisingMS.service.TagService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController("tagController")
@RequestMapping("/api/tag")
@Tag(name = "Tag Controller", description = "<p>Used for tag management: save/update/delete/return tags</p>")
public class TagController {

	@Autowired
	private TagService service;
	
	@Operation(summary = "Find all tags", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li><li>USER</li><li>ARTIST</li><li>DELIVERY</li></ul> ")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Tags found", content = {
			@Content(array = @ArraySchema(schema = @Schema(implementation = TagResponseDTO.class)))
		}),
		@ApiResponse(responseCode = "403", description = "Role authorization exception", content = {
			@Content(schema = @Schema(example = "The current user is not authorized to perform action"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "', '" + Roles.ARTIST + "', '" + Roles.DELIVERY + "')")
	public ResponseEntity<List<TagResponseDTO>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}
	
	@Operation(summary = "Find tag by id", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li><li>USER</li><li>ARTIST</li><li>DELIVERY</li></ul> ",
			parameters = {
				@Parameter(name = "id", description = "Tag id", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Tag found", content = {
			@Content(schema = @Schema(implementation = TagResponseDTO.class))
		}),
		@ApiResponse(responseCode = "403", description = "Role authorization exception", content = {
			@Content(schema = @Schema(example = "The current user is not authorized to perform action"))
		}),
		@ApiResponse(responseCode = "404", description = "Tag not found", content = {
			@Content(schema = @Schema(example = "The entity Tag with id '1' doesn't exist"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN +  "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "', '" + Roles.ARTIST + "', '" + Roles.DELIVERY + "')")
	public ResponseEntity<TagResponseDTO> findById(@PathVariable Integer id) throws NotFoundException {
		return ResponseEntity.ok(service.findById(id));
	}
	
	@Operation(summary = "Save tag", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li></ul> ")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "Tag saved", content = {
			@Content(schema = @Schema(implementation = TagResponseDTO.class))
		}),
		@ApiResponse(responseCode = "400", description = "The entered name is already in use", content = {
			@Content(schema = @Schema(example = "A Tag with the name 'tag' already exists"))
		}),
		@ApiResponse(responseCode = "403", description = "Role authorization exception", content = {
			@Content(schema = @Schema(example = "The current user is not authorized to perform action"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@PostMapping("")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "')")
	public ResponseEntity<TagResponseDTO> saveTag(@RequestBody @Valid TagRequestDTO request) throws NameAlreadyUsedException {
		return new ResponseEntity<>(service.saveTag(request), HttpStatus.CREATED);
	}
	
	@Operation(summary = "Update tag", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li></ul> ",
			parameters = {
				@Parameter(name = "id", description = "Tag id", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Tag updated", content = {
			@Content(schema = @Schema(implementation = TagResponseDTO.class))
		}),
		@ApiResponse(responseCode = "400", description = "The entered name is already in use", content = {
			@Content(schema = @Schema(example = "A Tag with the name 'tag' already exists"))
		}),
		@ApiResponse(responseCode = "403", description = "Role authorization exception", content = {
			@Content(schema = @Schema(example = "The current user is not authorized to perform action"))
		}),
		@ApiResponse(responseCode = "404", description = "Tag not found", content = {
			@Content(schema = @Schema(example = "The entity Tag with id '1' doesn't exist"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@PutMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "')")
	public ResponseEntity<TagResponseDTO> updateTag(@PathVariable Integer id, @RequestBody @Valid TagRequestDTO request) throws NotFoundException, NameAlreadyUsedException {
		return ResponseEntity.ok(service.updateTag(id, request));
	}
	
	@Operation(summary = "Delete tag", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li></ul> ",
			parameters = {
				@Parameter(name = "id", description = "Tag id", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Tag deleted", content = {
			@Content(schema = @Schema(implementation = TagResponseDTO.class))
		}),
		@ApiResponse(responseCode = "400", description = "The tag is the only one for some products", content = {
			@Content(schema = @Schema(example = "The tag 'tag1' cannot be deleted because is the only one for some products"))
		}),
		@ApiResponse(responseCode = "403", description = "Role authorization exception", content = {
			@Content(schema = @Schema(example = "The current user is not authorized to perform action"))
		}),
		@ApiResponse(responseCode = "404", description = "Tag not found", content = {
			@Content(schema = @Schema(example = "The entity Tag with id '1' doesn't exist"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "')")
	public ResponseEntity<TagResponseDTO> deleteTag(@PathVariable Integer id) throws NotFoundException, NoTagsException {
		return ResponseEntity.ok(service.deleteTag(id));
	}
}
