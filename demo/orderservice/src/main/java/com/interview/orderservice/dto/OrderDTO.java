package com.interview.orderservice.dto;

import com.interview.orderservice.enums.OrderState;
import com.interview.orderservice.enums.RejectionReason;

import lombok.Data;

@Data
public class OrderDTO {

	private Long id;
	private Long customerId;
	private double amount;
	private OrderState orderState;
	private RejectionReason rejectionReason;
	
}
