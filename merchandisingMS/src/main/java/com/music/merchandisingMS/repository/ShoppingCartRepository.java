package com.music.merchandisingMS.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.music.merchandisingMS.model.ShoppingCart;

@Repository("shoppingCart")
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Integer> {

	@Query("SELECT sc"
			+ " FROM ShoppingCart sc"
			+ " WHERE sc.userId = :userId")
	public Optional<ShoppingCart> findByUserId(Integer userId);
}
