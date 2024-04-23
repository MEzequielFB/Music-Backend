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

import com.music.musicMS.dto.PlaylistRequestDTO;
import com.music.musicMS.dto.PlaylistResponseDTO;
import com.music.musicMS.dto.PlaylistUpdateDTO;
import com.music.musicMS.dto.SongIdDTO;
import com.music.musicMS.dto.SongResponseDTO;
import com.music.musicMS.exception.AlreadyContainsSongException;
import com.music.musicMS.exception.AuthorizationException;
import com.music.musicMS.exception.DoNotContainsTheSongException;
import com.music.musicMS.exception.NameAlreadyUsedException;
import com.music.musicMS.exception.NotFoundException;
import com.music.musicMS.exception.PermissionsException;
import com.music.musicMS.model.Roles;
import com.music.musicMS.service.PlaylistService;

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

@RestController
@RequestMapping("/api/playlist")
@Tag(name = "Playlist Controller", description = "<p>Used for playlist management: create/delete/return playlists, add/remove songs to playlists and update playlists attributes</p>")
public class PlaylistController {
	
	@Autowired
	private PlaylistService service;
	
	@Operation(summary = "Find all playlists", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li><li>USER</li><li>ARTIST</li></ul> ",
			parameters = {
				@Parameter(name = "Authorization", description = "Authentication token provided when loggin in or registering", required = true)
			})
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "', '" + Roles.ARTIST + "')")
	public ResponseEntity<List<PlaylistResponseDTO>> findAll(@RequestHeader("Authorization") String token) {
		return ResponseEntity.ok(service.findAll(token));
	}
	
	@Operation(summary = "Find playlists by name", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li><li>USER</li><li>ARTIST</li></ul> ",
			parameters = {
				@Parameter(name = "name", description = "Playlist name", required = false),
				@Parameter(name = "Authorization", description = "Authentication token provided when loggin in or registering", required = true)
			})
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("/search")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "', '" + Roles.ARTIST + "')")
	public ResponseEntity<List<PlaylistResponseDTO>> findAllByName(@RequestParam(required = false) String name, @RequestHeader("Authorization") String token) {
		return ResponseEntity.ok(service.findAllByName(name, token));
	}
	
	@Operation(summary = "Find all playlists from the logged user", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li><li>USER</li><li>ARTIST</li></ul> ",
			parameters = {
				@Parameter(name = "Authorization", description = "Authentication token provided when loggin in or registering", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Playlists found", content = {
			@Content(array = @ArraySchema(schema = @Schema(implementation = PlaylistResponseDTO.class)))
		}),
		@ApiResponse(responseCode = "401", description = "No authenticated user", content = {
			@Content(schema = @Schema(example = "No authenticated user available"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("/user")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "', '" + Roles.ARTIST + "')")
	public ResponseEntity<List<PlaylistResponseDTO>> findAllByLoggedUser(@RequestHeader("Authorization") String token) throws AuthorizationException {
		return ResponseEntity.ok(service.findAllByLoggedUser(token));
	}
	
	@Operation(summary = "Find playlist by id", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li><li>USER</li><li>ARTIST</li></ul> ", 
			parameters = {
					@Parameter(name = "id", description = "Playlist id", required = true),
					@Parameter(name = "Authorization", description = "Authentication token provided when loggin in or registering", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Playlist found", content = {
			@Content(schema = @Schema(implementation = PlaylistResponseDTO.class))
		}),
		@ApiResponse(responseCode = "404", description = "Playlist/User not found", content = {
			@Content(schema = @Schema(example = "The entity Playlist/User with id '1'/'1' doesn't exist"))
		}),
		@ApiResponse(responseCode = "401", description = "No authenticated user", content = {
			@Content(schema = @Schema(example = "No authenticated user available"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "', '" + Roles.ARTIST + "')")
	public ResponseEntity<PlaylistResponseDTO> findById(@PathVariable Integer id, @RequestHeader("Authorization") String token) throws NotFoundException, AuthorizationException {
		return ResponseEntity.ok(service.findById(id, token));
	}
	
	@Operation(summary = "Get songs from playlist", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li><li>USER</li><li>ARTIST</li></ul> ",
			parameters = {
				@Parameter(name = "id", description = "Playlist id", required = true),
				@Parameter(name = "Authorization", description = "Authentication token provided when loggin in or registering", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Songs found", content = {
			@Content(array = @ArraySchema(schema = @Schema(implementation = SongResponseDTO.class)))
		}),
		@ApiResponse(responseCode = "404", description = "Playlist not found", content = {
			@Content(schema = @Schema(example = "The entity Playlist with id '1' doesn't exist"))
		}),
		@ApiResponse(responseCode = "401", description = "No authenticated user", content = {
			@Content(schema = @Schema(example = "No authenticated user available"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@GetMapping("/{id}/songs")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "', '" + Roles.ARTIST + "')")
	public ResponseEntity<List<SongResponseDTO>> getSongsFromPlaylist(@PathVariable Integer id, @RequestHeader("Authorization") String token) throws NotFoundException, AuthorizationException {
		return ResponseEntity.ok(service.getSongsFromPlaylist(id, token));
	}
	
	@Operation(summary = "Save playlist", description = "<p>Required roles:</p> <ul><li>USER</li></ul> ",
			parameters = {
				@Parameter(name = "Authorization", description = "Authentication token provided when loggin in or registering", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "Playlist saved", content = {
			@Content(schema = @Schema(implementation = PlaylistResponseDTO.class))
		}),
		@ApiResponse(responseCode = "404", description = "User not found", content = {
			@Content(schema = @Schema(example = "The entity User with id '1' doesn't exist"))
		}),
		@ApiResponse(responseCode = "401", description = "No authenticated user", content = {
			@Content(schema = @Schema(example = "No authenticated user available"))
		}),
		@ApiResponse(responseCode = "400", description = "A playlist with the entered name already exists", content = {
			@Content(schema = @Schema(example = "A Playlist with the name 'playlist1' already exists"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@PostMapping("")
	@PreAuthorize("hasAuthority('" + Roles.USER + "')")
	public ResponseEntity<PlaylistResponseDTO> savePlaylist(@RequestBody @Valid PlaylistRequestDTO request, @RequestHeader("Authorization") String token) throws NameAlreadyUsedException, NotFoundException, AuthorizationException {
		return new ResponseEntity<>(service.savePlaylist(request, token), HttpStatus.CREATED);
	}
	
	@Operation(summary = "Update playlist. The logged user should be the owner of the playlist", description = "<p>Required roles:</p> <ul><<li>USER</li></ul> ",
			parameters = {
				@Parameter(name = "id", description = "Playlist id", required = true),
				@Parameter(name = "Authorization", description = "Authentication token provided when loggin in or registering", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Playlist updated", content = {
			@Content(schema = @Schema(implementation = PlaylistResponseDTO.class))
		}),
		@ApiResponse(responseCode = "404", description = "Playlist/User not found", content = {
			@Content(schema = @Schema(example = "The entity Playlist/User with id '1'/'1' doesn't exist"))
		}),
		@ApiResponse(responseCode = "401", description = "No authenticated user", content = {
			@Content(schema = @Schema(example = "No authenticated user available"))
		}),
		@ApiResponse(responseCode = "400", description = "A playlist with the entered name already exists", content = {
			@Content(schema = @Schema(example = "A Playlist with the name 'playlist1' already exists"))
		}),
		@ApiResponse(responseCode = "403", description = "The current logged user cannot update the playlist of another user", content = {
			@Content(schema = @Schema(example = "The user with id 'id' doesn't have permissions to do the current action"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('" + Roles.USER + "')")
	public ResponseEntity<PlaylistResponseDTO> updatePlaylist(@PathVariable Integer id, @RequestBody @Valid PlaylistUpdateDTO request, @RequestHeader("Authorization") String token) throws NotFoundException, AuthorizationException, PermissionsException {
		return ResponseEntity.ok(service.updatePlaylist(id, request, token));
	}
	
	@Operation(summary = "Add song to playlist. The logged user should be the owner of the playlist", description = "<p>Required roles:</p> <ul><li>USER</li></ul> ",
			parameters = {
				@Parameter(name = "id", description = "Playlist id", required = true),
				@Parameter(name = "Authorization", description = "Authentication token provided when loggin in or registering", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Song added", content = {
			@Content(schema = @Schema(implementation = SongResponseDTO.class))
		}),
		@ApiResponse(responseCode = "404", description = "Playlist/Song not found", content = {
			@Content(schema = @Schema(example = "The entity Playlist/Song with id '1'/'1' doesn't exist"))
		}),
		@ApiResponse(responseCode = "401", description = "No authenticated user", content = {
			@Content(schema = @Schema(example = "No authenticated user available"))
		}),
		@ApiResponse(responseCode = "400", description = "The playlist already contains the entered song", content = {
			@Content(schema = @Schema(example = "The entity Playlist already contains the song"))
		}),
		@ApiResponse(responseCode = "403", description = "The current logged user cannot add a song to the playlist of another user", content = {
			@Content(schema = @Schema(example = "The user with id 'id' doesn't have permissions to do the current action"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@PutMapping("/{id}/addSong")
	@PreAuthorize("hasAuthority('" + Roles.USER + "')")
	public ResponseEntity<SongResponseDTO> addSong(@PathVariable Integer id, @RequestBody @Valid SongIdDTO request, @RequestHeader("Authorization") String token) throws NotFoundException, AlreadyContainsSongException, AuthorizationException, PermissionsException {
		return ResponseEntity.ok(service.addSong(id, request.getSongId(), token));
	}
	
	@Operation(summary = "Remove song from playlist. The logged user should be the owner of the playlist", description = "<p>Required roles:</p> <ul><li>USER</li></ul> ",
			parameters = {
				@Parameter(name = "id", description = "Playlist id", required = true),
				@Parameter(name = "Authorization", description = "Authentication token provided when loggin in or registering", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Song removed", content = {
			@Content(schema = @Schema(implementation = SongResponseDTO.class))
		}),
		@ApiResponse(responseCode = "404", description = "Playlist/Song not found", content = {
			@Content(schema = @Schema(example = "The entity Playlist/Song with id '1'/'1' doesn't exist"))
		}),
		@ApiResponse(responseCode = "401", description = "No authenticated user", content = {
			@Content(schema = @Schema(example = "No authenticated user available"))
		}),
		@ApiResponse(responseCode = "400", description = "The playlist do not contains the entered song", content = {
			@Content(schema = @Schema(example = "The playlist 'playlist1' do not contains the song 'song1'"))
		}),
		@ApiResponse(responseCode = "403", description = "The current logged user cannot add a song to the playlist of another user", content = {
			@Content(schema = @Schema(example = "The user with id 'id' doesn't have permissions to do the current action"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@PutMapping("/{id}/removeSong")
	@PreAuthorize("hasAuthority('" + Roles.USER + "')")
	public ResponseEntity<SongResponseDTO> removeSong(@PathVariable Integer id, @RequestBody @Valid SongIdDTO request, @RequestHeader("Authorization") String token) throws NotFoundException, AlreadyContainsSongException, AuthorizationException, DoNotContainsTheSongException, PermissionsException {
		return ResponseEntity.ok(service.removeSong(id, request.getSongId(), token));
	}
	
	@Operation(summary = "Delete playlist. The logged user should be the owner of the playlist or be an administrator", description = "<p>Required roles:</p> <ul><li>ADMIN</li><li>SUPER_ADMIN</li><li>USER</li></ul> ",
			parameters = {
				@Parameter(name = "id", description = "Playlist id", required = true),
				@Parameter(name = "Authorization", description = "Authentication token provided when loggin in or registering", required = true)
			})
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Song deleted", content = {
			@Content(schema = @Schema(implementation = SongResponseDTO.class))
		}),
		@ApiResponse(responseCode = "404", description = "Playlist/User not found", content = {
			@Content(schema = @Schema(example = "The entity Playlist/User with id '1'/'1' doesn't exist"))
		}),
		@ApiResponse(responseCode = "401", description = "No authenticated user", content = {
			@Content(schema = @Schema(example = "No authenticated user available"))
		}),
		@ApiResponse(responseCode = "403", description = "The current logged user cannot delete the playlist of another user", content = {
			@Content(schema = @Schema(example = "The user with id 'id' doesn't have permissions to do the current action"))
		})
	})
	@SecurityRequirement(name = "Bearer Authentication")
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('" + Roles.ADMIN + "', '" + Roles.SUPER_ADMIN + "', '" + Roles.USER + "')")
	public ResponseEntity<PlaylistResponseDTO> deletePlaylist(@PathVariable Integer id, @RequestHeader("Authorization") String token) throws NotFoundException, AuthorizationException, PermissionsException {
		return ResponseEntity.ok(service.deletePlaylist(id, token));
	}
}
