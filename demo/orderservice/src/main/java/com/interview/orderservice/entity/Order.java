package com.interview.orderservice.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.interview.orderservice.enums.OrderState;
import com.interview.orderservice.enums.RejectionReason;

import lombok.Data;

@Entity
@Data
@Table(name = "Orderr")// Order is reserver word in H2 database so we change table name as Orderr
public class Order {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private Long customerId;
	private double amount;
	@Enumerated(EnumType.STRING)
	private OrderState orderState;
	@Enumerated(EnumType.STRING)
	private RejectionReason rejectionReason;
	

}
