package com.music.merchandisingMS.controller;

import java.sql.SQLIntegrityConstraintViolationException;
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

import com.music.merchandisingMS.dto.StatusRequestDTO;
import com.music.merchandisingMS.dto.StatusResponseDTO;
import com.music.merchandisingMS.exception.NameAlreadyUsedException;
import com.music.merchandisingMS.exception.NotFoundException;
import com.music.merchandisingMS.model.Roles;
import com.music.merchandisingMS.service.StatusService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController("statusController")
@RequestMapping("/api/status")
public class StatusController {

	@Autowired
	private StatusService service;
	
	@Operation(summary = "Find all status", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li><li>DELIVERY</li></ul> ")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Status found", content = {
			@Content(array = @ArraySchema(schema = @Schema(implementation = StatusResponseDTO.class)))
		}),
		@ApiResponse(responseCode = "403", description = "Role authorization exception", content = {
			@Content(schema = @Schema(example = "The current user is not authorized to perform action"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.DELIVERY + "')")
	public ResponseEntity<List<StatusResponseDTO>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}
	
	@Operation(summary = "Find status by id", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li><li>DELIVERY</li></ul> ",
			parameters = {
				@Parameter(name = "id", description = "Status id", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Status found", content = {
			@Content(schema = @Schema(implementation = StatusResponseDTO.class))
		}),
		@ApiResponse(responseCode = "403", description = "Role authorization exception", content = {
			@Content(schema = @Schema(example = "The current user is not authorized to perform action"))
		}),
		@ApiResponse(responseCode = "404", description = "Status not found", content = {
			@Content(schema = @Schema(example = "The entity Status with id '1' doesn't exist"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.DELIVERY + "')")
	public ResponseEntity<StatusResponseDTO> findById(@PathVariable Integer id) throws NotFoundException {
		return ResponseEntity.ok(service.findById(id));
	}
	
	@Operation(summary = "Save status", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li></ul> ")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "Status saved", content = {
			@Content(schema = @Schema(implementation = StatusResponseDTO.class))
		}),
		@ApiResponse(responseCode = "400", description = "The entered name is already in use", content = {
			@Content(schema = @Schema(example = "A Status with the name 'status' already exists"))
		}),
		@ApiResponse(responseCode = "403", description = "Role authorization exception", content = {
			@Content(schema = @Schema(example = "The current user is not authorized to perform action"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@PostMapping("")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "')")
	public ResponseEntity<StatusResponseDTO> saveStatus(@RequestBody @Valid StatusRequestDTO request) throws NameAlreadyUsedException {
		return new ResponseEntity<StatusResponseDTO>(service.saveStatus(request), HttpStatus.CREATED);
	}
	
	@Operation(summary = "Update status", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li></ul> ",
			parameters = {
				@Parameter(name = "id", description = "Status id", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Status updated", content = {
			@Content(schema = @Schema(implementation = StatusResponseDTO.class))
		}),
		@ApiResponse(responseCode = "400", description = "The entered name is already in use", content = {
			@Content(schema = @Schema(example = "A Status with the name 'status' already exists"))
		}),
		@ApiResponse(responseCode = "403", description = "Role authorization exception", content = {
			@Content(schema = @Schema(example = "The current user is not authorized to perform action"))
		}),
		@ApiResponse(responseCode = "404", description = "Status not found", content = {
			@Content(schema = @Schema(example = "The entity Status with id '1' doesn't exist"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@PutMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "')")
	public ResponseEntity<StatusResponseDTO> updateStatus(@PathVariable Integer id, @RequestBody @Valid StatusRequestDTO request) throws NameAlreadyUsedException, NotFoundException {
		return ResponseEntity.ok(service.updateStatus(id, request));
	}
	
	@Operation(summary = "Delete status", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li></ul> ",
			parameters = {
				@Parameter(name = "id", description = "Status id", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Status deleted", content = {
			@Content(schema = @Schema(implementation = StatusResponseDTO.class))
		}),
		@ApiResponse(responseCode = "400", description = "The status is linked to some order OR foreig key constraint fail when deleting", content = {
			@Content(schema = @Schema(example = "A Status with the name 'status' already exists OR Cannot delete or update a parent row: a foreign key constraint fails"))
		}),
		@ApiResponse(responseCode = "403", description = "Role authorization exception", content = {
			@Content(schema = @Schema(example = "The current user is not authorized to perform action"))
		}),
		@ApiResponse(responseCode = "404", description = "Status not found", content = {
			@Content(schema = @Schema(example = "The entity Status with id '1' doesn't exist"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "')")
	public ResponseEntity<StatusResponseDTO> deleteStatus(@PathVariable Integer id) throws NotFoundException, SQLIntegrityConstraintViolationException {
		return ResponseEntity.ok(service.deleteStatus(id));
	}
}
