package com.music.musicMS.dto;

import java.util.List;

import com.music.musicMS.model.Album;

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
public class AlbumResponseDTO {
	@Schema(name = "id", example = "1")
	private Integer id;
	
	@Schema(name = "name", example = "album1")
	private String name;
	
	@Schema(implementation = ArtistResponseDTO.class)
	private ArtistResponseDTO owner;
	
	@ArraySchema(schema = @Schema(implementation = ArtistResponseDTO.class))
	private List<ArtistResponseDTO> artists;
	
	@ArraySchema(schema = @Schema(implementation = SongResponseDTO.class))
	private List<SongResponseDTO> songs;
	
	public AlbumResponseDTO(Album album) {
		this.id = album.getId();
		this.name = album.getName();
		this.owner = new ArtistResponseDTO(album.getOwner());
		this.artists = album.getArtists().stream().map( ArtistResponseDTO::new ).toList();
		this.songs = album.getSongs().stream().map( SongResponseDTO::new ).toList();
	}
}
