package com.interview.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.interview.orderservice.enums.OrderState;
import com.interview.orderservice.enums.RejectionReason;

import lombok.Data;

@Data
public class SaveOrderResponseDTO {
	
	private OrderState orderState;
	@JsonInclude(Include.NON_NULL)
	private RejectionReason rejectionReason;

}
