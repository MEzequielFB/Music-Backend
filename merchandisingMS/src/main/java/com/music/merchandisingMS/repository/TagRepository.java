package com.music.merchandisingMS.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.music.merchandisingMS.model.Tag;

@Repository("tagRepository")
public interface TagRepository extends JpaRepository<Tag, Integer> {

	@Query("SELECT t"
			+ " FROM Tag t"
			+ " WHERE t.id IN :ids")
	public Set<Tag> findAllByIds(List<Integer> ids);
	
	public Optional<Tag> findByName(String name);
}
