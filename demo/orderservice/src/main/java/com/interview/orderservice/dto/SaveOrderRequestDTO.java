package com.interview.orderservice.dto;

import lombok.Data;

@Data
public class SaveOrderRequestDTO {
	
	private Long customerId;
	private double amount;

}
