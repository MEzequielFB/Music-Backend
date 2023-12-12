package com.music.userMS.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.music.userMS.dto.FollowRequestDTO;
import com.music.userMS.dto.UserFollowerResponseDTO;
import com.music.userMS.exception.NotFoundException;
import com.music.userMS.model.User;
import com.music.userMS.model.UserFollowers;
import com.music.userMS.repository.UserFollowerRepository;
import com.music.userMS.repository.UserRepository;

@Service(value = "userFollowerService")
public class UserFollowerService {

	@Autowired
	private UserFollowerRepository repository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Transactional
	//public UserFollowerResponseDTO followUser(int userId, int followedUserId) throws NotFoundException {
	public UserFollowerResponseDTO followUser(FollowRequestDTO request) throws NotFoundException {
		Optional<User> userOptional = userRepository.findById(request.getUserId());
		Optional<User> followedUserOptional = userRepository.findById(request.getFollowedUserId());
		
		if (!userOptional.isPresent()) {
			throw new NotFoundException("User", request.getUserId());
		}
		if (!followedUserOptional.isPresent()) {
			throw new NotFoundException("User", request.getFollowedUserId());
		}
		
		Date currentDate = new Date(System.currentTimeMillis());
		UserFollowers userFollower = new UserFollowers(userOptional.get(), followedUserOptional.get(), currentDate);
		userFollower = repository.save(userFollower);
		
		return new UserFollowerResponseDTO(userFollower); 
	}
}
