package com.interview.orderservice.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.interview.orderservice.entity.Order;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long>{

	List<Order> findByCustomerId (Long customerId);
}
