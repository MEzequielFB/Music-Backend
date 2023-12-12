package com.music.userMS.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ArtistsByIdsDTO {
	private List<Integer> artistsId;
}
