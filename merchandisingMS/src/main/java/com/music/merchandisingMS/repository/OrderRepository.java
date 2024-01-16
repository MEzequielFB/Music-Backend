package com.music.merchandisingMS.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.music.merchandisingMS.model.Order;
import com.music.merchandisingMS.model.Product;

@Repository("orderRepository")
public interface OrderRepository extends JpaRepository<Order, Integer> {

	@Query("SELECT o"
			+ " FROM Order o"
			+ " WHERE :product IN elements(o.products)")
	public List<Order> findAllByProduct(Product product);
}
