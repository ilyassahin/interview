package com.interview.customerservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.interview.customerservice.enums.RejectionReason;
import com.interview.customerservice.enums.State;

import lombok.Data;

@Data
public class CheckCreditLimitResponseDTO {
	
	private State state;
	@JsonInclude(Include.NON_NULL)
	private RejectionReason rejectionReason;

}
