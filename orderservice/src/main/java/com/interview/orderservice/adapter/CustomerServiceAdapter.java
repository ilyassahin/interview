package com.interview.orderservice.adapter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.interview.orderservice.dto.CheckCreditLimitRequestDTO;
import com.interview.orderservice.dto.CheckCreditLimitResponseDTO;

@Service
public class CustomerServiceAdapter {

	@Value("${customerserviceuri}")
	private String customerServiceUri;
	
	private RestTemplate restTemplate;
	
	public CustomerServiceAdapter() {
		restTemplate = new RestTemplate();
	}

	public CheckCreditLimitResponseDTO checkCreditLimit(CheckCreditLimitRequestDTO requestDTO) {
		return restTemplate.postForObject(customerServiceUri, requestDTO, CheckCreditLimitResponseDTO.class);
	}
}
