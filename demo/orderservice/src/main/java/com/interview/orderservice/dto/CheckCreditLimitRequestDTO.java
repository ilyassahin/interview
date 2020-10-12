package com.interview.orderservice.dto;

import lombok.Data;

@Data
public class CheckCreditLimitRequestDTO {
	
	private Long customerId;
	private double amount;
}
