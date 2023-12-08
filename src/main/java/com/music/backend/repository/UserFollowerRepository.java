package com.music.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.music.backend.model.UserFollowers;
import com.music.backend.model.UserFollowersPK;

@Repository(value = "userFolloweRepository")
public interface UserFollowerRepository extends JpaRepository<UserFollowers, UserFollowersPK> {

}
