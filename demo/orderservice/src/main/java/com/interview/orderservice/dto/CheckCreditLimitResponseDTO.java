package com.interview.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.interview.orderservice.enums.RejectionReason;
import com.interview.orderservice.enums.State;

import lombok.Data;

@Data
public class CheckCreditLimitResponseDTO {

	private State state;
	@JsonInclude(Include.NON_NULL)
	private RejectionReason rejectionReason;

}
