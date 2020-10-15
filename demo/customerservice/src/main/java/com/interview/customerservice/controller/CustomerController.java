package com.interview.customerservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.interview.customerservice.dto.CheckCreditLimitRequestDTO;
import com.interview.customerservice.dto.CheckCreditLimitResponseDTO;
import com.interview.customerservice.dto.CustomerDTO;
import com.interview.customerservice.dto.SaveCustomerResponse;
import com.interview.customerservice.enums.State;
import com.interview.customerservice.service.CustomerService;

@RestController
@RequestMapping("/customer")
public class CustomerController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	CustomerService customerService;

	@PostMapping("/save")
	public SaveCustomerResponse saveCustomer(@RequestBody CustomerDTO customerDTO) {

		SaveCustomerResponse response = new SaveCustomerResponse();
		try {
			long id = customerService.saveCustomer(customerDTO);
			log.info("Customer saved!, ID: {}", id);
			response.setCustomerId(id);
			response.setState(State.SUCCESS);
		} catch (Exception e) {
			response.setState(State.FAIL);
			log.error(e.getMessage());
		}
		return response;
	}

	@PostMapping("/checkCreditLimit")
	public CheckCreditLimitResponseDTO checkCreditLimit(@RequestBody CheckCreditLimitRequestDTO requestDTO) {
		return customerService.checkCreditLimit(requestDTO);
	}

	@GetMapping("/getCustomerById")
	public CustomerDTO getCustomerById(@RequestParam Long customerId) {
		return customerService.getCustomerById(customerId);
	}

}
