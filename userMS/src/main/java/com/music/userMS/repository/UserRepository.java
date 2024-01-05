package com.music.userMS.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.music.userMS.model.Account;
import com.music.userMS.model.User;

@Repository(value = "userRepository")
public interface UserRepository extends JpaRepository<User, Integer> {
	
	@Query("SELECT fu"
			+ " FROM User u"
			+ " JOIN u.followedUsers fu"
			+ " WHERE u.id = :id")
	public List<User> findFollowedUsersById(int id);
	
	@Query("SELECT f"
			+ " FROM User u"
			+ " JOIN u.followers f"
			+ " WHERE u.id = :id")
	public List<User> findFollowersById(int id);
	
	@Query("SELECT u"
			+ " FROM User u"
			+ " WHERE u.email = :email")
	public Optional<User> findByEmail(String email);
	
	@Query("SELECT u"
			+ " FROM User u"
			+ " WHERE u.id IN :ids")
	public List<User> findAllByIds(List<Integer> ids);
}
