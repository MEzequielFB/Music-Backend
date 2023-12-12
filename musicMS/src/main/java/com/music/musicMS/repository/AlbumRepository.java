package com.music.musicMS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.music.musicMS.model.Album;

@Repository(value = "albumRepository")
public interface AlbumRepository extends JpaRepository<Album, Integer> {

}
