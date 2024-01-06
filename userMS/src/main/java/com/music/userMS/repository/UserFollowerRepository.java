package com.music.userMS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.music.userMS.model.UserFollowers;
import com.music.userMS.model.UserFollowersPK;

@Repository(value = "userFolloweRepository")
public interface UserFollowerRepository extends JpaRepository<UserFollowers, UserFollowersPK> {

}
