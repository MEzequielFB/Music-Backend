package com.music.musicMS.dto;

import java.util.List;

import com.music.musicMS.model.Song;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SongResponseDTO {
	@Schema(name = "id", example = "1")
	private Integer id;
	
	@Schema(name = "name", example = "song1")
	private String name;
	
	@Schema(name = "reproductions", example = "100")
	private Integer reproductions;
	
	@Schema(name = "duration", example = "240")
	private Integer duration; // seconds
	
	@Schema(name = "album", example = "album1")
	private String album;
	
	@ArraySchema(schema = @Schema(implementation = ArtistResponseDTO.class))
	private List<ArtistResponseDTO> artists;
	
	@ArraySchema(schema = @Schema(implementation = GenreResponseDTO.class))
	private List<GenreResponseDTO> genres;
	
	public SongResponseDTO(Song song) {
		this.id = song.getId();
		this.name = song.getName();
		this.reproductions = song.getReproductions();
		this.duration = song.getDuration();
		this.album = song.getAlbum() != null ? song.getAlbum().getName() : "";
		this.artists = song.getArtists().stream().map( ArtistResponseDTO::new ).toList();
		this.genres = song.getGenres().stream().map( GenreResponseDTO::new ).toList();
	}
}
