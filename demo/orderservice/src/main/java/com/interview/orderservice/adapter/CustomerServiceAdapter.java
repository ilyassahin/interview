package com.interview.orderservice.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.interview.orderservice.dto.CheckCreditLimitRequestDTO;
import com.interview.orderservice.dto.CheckCreditLimitResponseDTO;

@Service
public class CustomerServiceAdapter {

	private String customerServiceUri = "http://customerservice/customer/checkCreditLimit";
	
    @Autowired
    RestTemplate restTemplate;
	
	public CheckCreditLimitResponseDTO checkCreditLimit(CheckCreditLimitRequestDTO requestDTO) {
		return restTemplate.postForObject(customerServiceUri, requestDTO, CheckCreditLimitResponseDTO.class);
	}
	
	@Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
