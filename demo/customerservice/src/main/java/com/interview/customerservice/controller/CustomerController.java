package com.interview.customerservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interview.customerservice.consts.Detail;
import com.interview.customerservice.consts.State;
import com.interview.customerservice.dto.CheckCreditLimitRequestDTO;
import com.interview.customerservice.dto.CheckCreditLimitResponseDTO;
import com.interview.customerservice.dto.CustomerDTO;
import com.interview.customerservice.dto.SaveCustomerResponseDTO;
import com.interview.customerservice.entity.Customer;
import com.interview.customerservice.repository.CustomerRepository;

@RestController
@RequestMapping("/customer")
public class CustomerController {

	@Autowired
	CustomerRepository customerRepository;
	
	private final Logger log = LoggerFactory.getLogger(this.getClass()); 
	
	@PostMapping("/save")
	public SaveCustomerResponseDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {

		SaveCustomerResponseDTO responseDTO = new SaveCustomerResponseDTO();
		try {
			Customer customer = new Customer();
			customer.setName(customerDTO.getName());
			customer.setCreditLimit(customerDTO.getCreditLimit());
			customer.setCreditReservation(customerDTO.getCreditReservation());
			customerRepository.save(customer);
			responseDTO.setState(State.SUCCESS);
			responseDTO.setCustomerId(customer.getId());
			log.info("Customer saved!");
		} catch (Exception e) {
			responseDTO.setState(State.FAIL);
			log.error(e.getMessage());
		}
		return responseDTO;
	}
	
	@PostMapping("/checkCreditLimit")
	public CheckCreditLimitResponseDTO checkCreditLimit(@RequestBody CheckCreditLimitRequestDTO requestDTO) {

		Optional<Customer> op = customerRepository.findById(requestDTO.getCustomerId());
		CheckCreditLimitResponseDTO responseDTO = new CheckCreditLimitResponseDTO();
		
		if(op.isPresent()) {
			Customer customer = op.get();
			if (customer.getCreditLimit() >= requestDTO.getAmount()) {
				customer.setCreditLimit(customer.getCreditLimit() - requestDTO.getAmount());
				customer.setCreditReservation(customer.getCreditReservation() + requestDTO.getAmount());
				customerRepository.save(customer);
				responseDTO.setState(State.SUCCESS);
			}else {
				responseDTO.setState(State.FAIL);
				responseDTO.setDetail(Detail.INSUFFICIENT_CREDIT);
			}
		}else {
			responseDTO.setState(State.FAIL);
			responseDTO.setDetail(Detail.UNKNOWN_CUSTOMER);
		}
		return responseDTO;
	}
	
	@GetMapping("/getCustomerById")
	public Customer getCustomerById(@RequestParam Long customerId) {
		Optional<Customer> op = customerRepository.findById(customerId);
		return op.isPresent() ? op.get() : null;
	}

}
