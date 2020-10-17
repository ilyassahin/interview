package com.interview.customerservice.dto;

import com.interview.customerservice.enums.OrderState;
import com.interview.customerservice.enums.RejectionReason;

import lombok.Data;

@Data
public class OrderDTO {

	private Long id;
	private Long customerId;
	private double amount;
	private OrderState orderState;
	private RejectionReason rejectionReason;
	
}
