package com.music.merchandisingMS.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.music.merchandisingMS.model.Status;

@Repository("statusRepository")
public interface StatusRepository extends JpaRepository<Status, Integer> {

	public Optional<Status> findByName(String name);
}
