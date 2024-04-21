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

import com.music.userMS.dto.AccountRequestDTO;
import com.music.userMS.dto.AccountResponseDTO;
import com.music.userMS.dto.BalanceDTO;
import com.music.userMS.dto.UserIdDTO;
import com.music.userMS.exception.AddUserException;
import com.music.userMS.exception.AlreadyContainsException;
import com.music.userMS.exception.AuthorizationException;
import com.music.userMS.exception.MultipleUsersLinkedToAccountException;
import com.music.userMS.exception.NotEnoughBalanceException;
import com.music.userMS.exception.NotFoundException;
import com.music.userMS.exception.PermissionsException;
import com.music.userMS.model.Roles;
import com.music.userMS.service.AccountService;

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

@RestController(value = "accountController")
@RequestMapping("api/account")
@Tag(name = "Account Controller", description = "<p>Used for account management: create accounts, add/remove users to accounts, add/remove balance from accounts, deleted accounts and return accounts</p>")
public class AccountController {

	@Autowired
	private AccountService service;
	
	@Operation(summary = "Find all accounts", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li></ul> ")
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("")
	@PreAuthorize( "hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "')" )
	public ResponseEntity<List<AccountResponseDTO>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}
	
	@Operation(summary = "Find account by id", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li><li>USER</li></ul> ",
			parameters = {
				@Parameter(name = "id", description = "Account id", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode =  "200", description = "Account found", content = {
			@Content(schema = @Schema(implementation = AccountResponseDTO.class))
		}),
		@ApiResponse(responseCode =  "404", description = "Account not found", content = {
			@Content(schema = @Schema(example = "The entity Account with id '1' doesn't exist"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("/{id}")
	@PreAuthorize( "hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "')" )
	public ResponseEntity<AccountResponseDTO> findById(@PathVariable Integer id) throws NotFoundException {
		return ResponseEntity.ok(service.findById(id));
	}
	
	@Operation(summary = "Find all accounts by userId", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li><li>USER</li></ul> ",
			parameters = {
				@Parameter(name = "userId", description = "User id", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode =  "200", description = "Accounts found", content = {
			@Content(array = @ArraySchema(schema = @Schema(implementation = AccountResponseDTO.class)))
		}),
		@ApiResponse(responseCode =  "404", description = "User not found", content = {
			@Content(schema = @Schema(example = "A User with the id '1' doesn't exist"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("/user/{userId}")
	@PreAuthorize( "hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "')" )
	public ResponseEntity<List<AccountResponseDTO>> findByAllByUser(@PathVariable Integer userId) throws NotFoundException {
		return ResponseEntity.ok(service.findByAllByUser(userId));
	}
	
	@Operation(summary = "Find all accounts by logged user", description = "<p>Required roles:</p> <ul><li>USER</li></ul> ",
			parameters = {
				@Parameter(name = "Authorization", description = "Authentication token provided when login or register", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode =  "200", description = "Accounts found", content = {
			@Content(array = @ArraySchema(schema = @Schema(implementation = AccountResponseDTO.class)))
		}),
		@ApiResponse(responseCode =  "404", description = "User not found", content = {
			@Content(schema = @Schema(example = "A User with the id '1' doesn't exist"))
		}),
		@ApiResponse(responseCode =  "401", description = "No authenticated user", content = {
			@Content(schema = @Schema(example = "No authenticated user available"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("/loggedUser")
	@PreAuthorize("hasAuthority('" + Roles.USER + "')")
	public ResponseEntity<List<AccountResponseDTO>> findByAllByLoggedUser(@RequestHeader("Authorization") String token) throws NotFoundException, AuthorizationException {
		return ResponseEntity.ok(service.findByAllByLoggedUser(token));
	}
	
	@Operation(summary = "Save account", description = "<p>Required roles:</p> <ul><li>USER</li></ul> ",
			parameters = {
				@Parameter(name = "Authorization", description = "Authentication token provided when login or register", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode =  "201", description = "Account saved", content = {
			@Content(schema = @Schema(implementation = AccountResponseDTO.class))
		}),
		@ApiResponse(responseCode =  "404", description = "User not found", content = {
			@Content(schema = @Schema(example = "A User with the id '1' doesn't exist"))
		}),
		@ApiResponse(responseCode =  "401", description = "No authenticated user", content = {
			@Content(schema = @Schema(example = "No authenticated user available"))
		}),
		@ApiResponse(responseCode =  "400", description = "The user entered to be added is not a regular user", content = {
			@Content(schema = @Schema(example = "Cannot add the user with id '1' because is not a regular user"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@PostMapping("")
	@PreAuthorize("hasAuthority('" + Roles.USER + "')")
	public ResponseEntity<AccountResponseDTO> saveAccount(@RequestBody @Valid AccountRequestDTO request, @RequestHeader("Authorization") String token) throws AuthorizationException, NotFoundException, AddUserException {
		return new ResponseEntity<AccountResponseDTO>(service.saveAccount(request, token), HttpStatus.CREATED);
	}
	
	@Operation(summary = "Add a user to an account. The logged user should be linked to the account to add another user", description = "<p>Required roles:</p> <ul><li>USER</li></ul> ",
			parameters = {
				@Parameter(name = "id", description = "Account id", required = true),
				@Parameter(name = "Authorization", description = "Authentication token provided when login or register", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode =  "200", description = "User added", content = {
			@Content(schema = @Schema(implementation = AccountResponseDTO.class))
		}),
		@ApiResponse(responseCode =  "404", description = "User or Account not found", content = {
			@Content(schema = @Schema(example = "A User/Account with the id '1'/'1' doesn't exist"))
		}),
		@ApiResponse(responseCode =  "401", description = "No authenticated user", content = {
			@Content(schema = @Schema(example = "No authenticated user available"))
		}),
		@ApiResponse(responseCode =  "400", description = "The user entered to be added is not a regular user OR is already linked to the account", content = {
			@Content(schema = @Schema(example = "Cannot add the user with id '1' because is not a regular user OR is already linked to the account"))
		}),
		@ApiResponse(responseCode =  "403", description = "Permissions error", content = {
			@Content(schema = @Schema(example = "The user with id '1' doesn't have permissions to do the current action"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@PutMapping("/{id}/addUser")
	@PreAuthorize("hasAuthority('" + Roles.USER + "')")
	public ResponseEntity<AccountResponseDTO> addUser(@PathVariable Integer id, @RequestBody @Valid UserIdDTO request, @RequestHeader("Authorization") String token) throws NotFoundException, AlreadyContainsException, AuthorizationException, AddUserException, PermissionsException {
		return ResponseEntity.ok(service.addUser(id, request.getUserId(), token));
	}
	
	@Operation(summary = "Remove a user from an account. The logged user should be linked to the account to remove another user", description = "<p>Required roles:</p> <ul><li>USER</li></ul> ",
			parameters = {
				@Parameter(name = "id", description = "Account id", required = true),
				@Parameter(name = "Authorization", description = "Authentication token provided when login or register", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode =  "200", description = "User removed", content = {
			@Content(schema = @Schema(implementation = AccountResponseDTO.class))
		}),
		@ApiResponse(responseCode =  "404", description = "User or Account not found", content = {
			@Content(schema = @Schema(example = "A User/Account with the id '1'/'1' doesn't exist"))
		}),
		@ApiResponse(responseCode =  "401", description = "No authenticated user", content = {
			@Content(schema = @Schema(example = "No authenticated user available"))
		}),
		@ApiResponse(responseCode =  "403", description = "Permissions error", content = {
			@Content(schema = @Schema(example = "The user with id '1' doesn't have permissions to do the current action"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@PutMapping("/{id}/removeUser")
	@PreAuthorize("hasAuthority('" + Roles.USER + "')")
	public ResponseEntity<AccountResponseDTO> removeUser(@PathVariable Integer id, @RequestBody @Valid UserIdDTO request, @RequestHeader("Authorization") String token) throws NotFoundException, AuthorizationException, PermissionsException {
		return ResponseEntity.ok(service.removeUser(id, request.getUserId(), token));
	}
	
	@Operation(summary = "Add balance to an account. The logged user should be linked to the account or be an administrator", description = "<p>Required roles:</p> <ul><li>USER</li><li>ADMIN</li><li>SUPER_ADMIN</li></ul> ",
			parameters = {
				@Parameter(name = "id", description = "Account id", required = true),
				@Parameter(name = "Authorization", description = "Authentication token provided when login or register", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode =  "200", description = "Balance added", content = {
			@Content(schema = @Schema(implementation = AccountResponseDTO.class))
		}),
		@ApiResponse(responseCode =  "404", description = "User or Account not found", content = {
			@Content(schema = @Schema(example = "A User/Account with the id '1'/'1' doesn't exist"))
		}),
		@ApiResponse(responseCode =  "401", description = "No authenticated user", content = {
			@Content(schema = @Schema(example = "No authenticated user available"))
		}),
		@ApiResponse(responseCode =  "403", description = "Permissions error", content = {
			@Content(schema = @Schema(example = "The user with id '1' doesn't have permissions to do the current action"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@PutMapping("/{id}/addBalance")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "')")
	public ResponseEntity<AccountResponseDTO> addBalance(@PathVariable Integer id, @RequestBody @Valid BalanceDTO request, @RequestHeader("Authorization") String token) throws NotFoundException, AuthorizationException, PermissionsException {
		return ResponseEntity.ok(service.addBalance(id, request, token));
	}
	
	@Operation(summary = "Remove balance from an account. The logged user should be linked to the account or be an administrator", description = "<p>Required roles:</p> <ul><li>USER</li><li>ADMIN</li><li>SUPER_ADMIN</li></ul> ",
			parameters = {
				@Parameter(name = "id", description = "Account id", required = true),
				@Parameter(name = "Authorization", description = "Authentication token provided when login or register", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode =  "200", description = "Balance removed", content = {
			@Content(schema = @Schema(implementation = AccountResponseDTO.class))
		}),
		@ApiResponse(responseCode =  "404", description = "User or Account not found", content = {
			@Content(schema = @Schema(example = "A User/Account with the id '1'/'1' doesn't exist"))
		}),
		@ApiResponse(responseCode =  "401", description = "No authenticated user", content = {
			@Content(schema = @Schema(example = "No authenticated user available"))
		}),
		@ApiResponse(responseCode =  "403", description = "Permissions error", content = {
			@Content(schema = @Schema(example = "The user with id '1' doesn't have permissions to do the current action"))
		}),
		@ApiResponse(responseCode =  "400", description = "Not enough balance", content = {
			@Content(schema = @Schema(example = "The account with id '1' doesn't have enough balance for the transaction"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@PutMapping("/{id}/removeBalance")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "')")
	public ResponseEntity<AccountResponseDTO> removeBalance(@PathVariable Integer id, @RequestBody @Valid BalanceDTO request, @RequestHeader("Authorization") String token) throws NotFoundException, NotEnoughBalanceException, AuthorizationException, PermissionsException {
		return ResponseEntity.ok(service.removeBalance(id, request, token));
	}
	
	@Operation(summary = "Delete an account. The logged user should be linked to the account or be an administrator. There should be one or zero users linked to the account for delete it", description = "<p>Required roles:</p> <ul><li>USER</li><li>ADMIN</li><li>SUPER_ADMIN</li></ul> ",
			parameters = {
				@Parameter(name = "id", description = "Account id", required = true),
				@Parameter(name = "Authorization", description = "Authentication token provided when login or register", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode =  "200", description = "Account deleted", content = {
			@Content(schema = @Schema(implementation = AccountResponseDTO.class))
		}),
		@ApiResponse(responseCode =  "404", description = "User or Account not found", content = {
			@Content(schema = @Schema(example = "A User/Account with the id '1'/'1' doesn't exist"))
		}),
		@ApiResponse(responseCode =  "401", description = "No authenticated user", content = {
			@Content(schema = @Schema(example = "No authenticated user available"))
		}),
		@ApiResponse(responseCode =  "403", description = "Permissions error", content = {
			@Content(schema = @Schema(example = "The user with id '1' doesn't have permissions to do the current action"))
		}),
		@ApiResponse(responseCode =  "400", description = "Multiple users linked to the account", content = {
			@Content(schema = @Schema(example = "Cannot delete the account with id '1' because it has multiple users linked"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "')")
	public ResponseEntity<AccountResponseDTO> deleteAccount(@PathVariable Integer id, @RequestHeader("Authorization") String token) throws NotFoundException, MultipleUsersLinkedToAccountException, AuthorizationException, PermissionsException {
		return ResponseEntity.ok(service.deleteAccount(id, token));
	}
}
