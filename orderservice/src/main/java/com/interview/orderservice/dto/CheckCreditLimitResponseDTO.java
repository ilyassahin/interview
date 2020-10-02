package com.interview.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
public class CheckCreditLimitResponseDTO {

	private String state;
	@JsonInclude(Include.NON_NULL)
	private String detail;

}
