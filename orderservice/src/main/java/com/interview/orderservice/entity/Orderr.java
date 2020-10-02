package com.interview.orderservice.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Orderr {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private Long customerId;
	private double amount;
	private String state;
	private String rejectionReason;
	

}
