package com.music.merchandisingMS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.music.merchandisingMS.model.Order;

@Repository("orderRepository")
public interface OrderRepository extends JpaRepository<Order, Integer> {

}
