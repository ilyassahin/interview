package com.interview.customerservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.interview.customerservice.entity.Customer;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long>{

}
