package com.interview.customerservice.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interview.customerservice.dto.CheckCreditLimitRequestDTO;
import com.interview.customerservice.dto.CheckCreditLimitResponseDTO;
import com.interview.customerservice.dto.CustomerDTO;
import com.interview.customerservice.entity.Customer;
import com.interview.customerservice.enums.RejectionReason;
import com.interview.customerservice.enums.State;
import com.interview.customerservice.repository.CustomerRepository;

@Service
public class CustomerService {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	CustomerRepository customerRepository;

	public long saveCustomer(CustomerDTO customerDTO) {
		Customer customer = modelMapper.map(customerDTO, Customer.class);
		customerRepository.save(customer);
		return customer.getId();
	}

	public CustomerDTO getCustomerById(Long customerId) {
		Optional<Customer> op = customerRepository.findById(customerId);
		return op.isPresent() ? modelMapper.map(op.get(),CustomerDTO.class) : null;
	}
	
	public CheckCreditLimitResponseDTO checkCreditLimit(CheckCreditLimitRequestDTO requestDTO) {
	
		Optional<Customer> op = customerRepository.findById(requestDTO.getCustomerId());
		CheckCreditLimitResponseDTO responseDTO = new CheckCreditLimitResponseDTO();

		if (op.isPresent()) {
			Customer customer = op.get();
			if (customer.getCreditLimit() >= requestDTO.getAmount()) {
				customer.setCreditLimit(customer.getCreditLimit() - requestDTO.getAmount());
				customer.setCreditReservation(customer.getCreditReservation() + requestDTO.getAmount());
				customerRepository.save(customer);
				responseDTO.setState(State.SUCCESS);
			} else {
				responseDTO.setState(State.FAIL);
				responseDTO.setRejectionReason(RejectionReason.INSUFFICIENT_CREDIT);
			}
		} else {
			responseDTO.setState(State.FAIL);
			responseDTO.setRejectionReason(RejectionReason.UNKNOWN_CUSTOMER);
		}
		return responseDTO;
	}

}
