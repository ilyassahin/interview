package com.interview.customerservice.dto;

import com.interview.customerservice.enums.State;

import lombok.Data;

@Data
public class SaveCustomerResponseDTO {
	
	private State state;
	private long customerId;

}
