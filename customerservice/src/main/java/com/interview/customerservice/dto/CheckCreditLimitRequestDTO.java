package com.interview.customerservice.dto;

import lombok.Data;

@Data
public class CheckCreditLimitRequestDTO {
	
	private Long customerId;
	private double amount;
}
