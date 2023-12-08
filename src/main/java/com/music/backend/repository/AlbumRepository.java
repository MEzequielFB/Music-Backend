package com.music.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.music.backend.model.Album;

@Repository(value = "albumRepository")
public interface AlbumRepository extends JpaRepository<Album, Integer> {

}
