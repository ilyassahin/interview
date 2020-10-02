package com.interview.orderservice.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.interview.orderservice.entity.Orderr;

@Repository
public interface OrderRepository extends CrudRepository<Orderr, Long>{

	List<Orderr> findByCustomerId (Long customerId);
}
