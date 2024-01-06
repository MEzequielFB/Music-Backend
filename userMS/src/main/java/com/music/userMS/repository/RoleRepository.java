package com.music.userMS.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.music.userMS.model.Role;

@Repository("roleRepository")
public interface RoleRepository extends JpaRepository<Role, Integer> {

	Optional<Role> findByName(String name);

}
