package com.music.merchandisingMS.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.music.merchandisingMS.model.Tag;

@Repository("tagRepository")
public interface TagRepository extends JpaRepository<Tag, Integer> {

	public Optional<Tag> findByName(String name);
}
