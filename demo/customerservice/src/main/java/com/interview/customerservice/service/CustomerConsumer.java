package com.interview.customerservice.service;

import java.io.IOException;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.interview.customerservice.dto.OrderDTO;
import com.interview.customerservice.entity.Customer;
import com.interview.customerservice.enums.OrderState;
import com.interview.customerservice.enums.RejectionReason;
import com.interview.customerservice.port.ICustomerMessaging;
import com.interview.customerservice.port.ICustomerPublisher;
import com.interview.customerservice.repository.CustomerRepository;

public class CustomerConsumer implements ICustomerMessaging {

	private static final Logger logger = LoggerFactory.getLogger(CustomerConsumer.class);

	@Autowired
	private ICustomerPublisher customerPublisher;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public void consumeMessage(String content) {

		try {
			OrderDTO orderDTO = modelMapper.map(content, OrderDTO.class);

			Optional<Customer> op = customerRepository.findById(orderDTO.getCustomerId());

			if (op.isPresent()) {
				Customer customer = op.get();
				if (customer.getCreditLimit() >= orderDTO.getAmount()) {
					customer.setCreditLimit(customer.getCreditLimit() - orderDTO.getAmount());
					customer.setCreditReservation(customer.getCreditReservation() + orderDTO.getAmount());
					customerRepository.save(customer);
				} else {
					orderDTO.setOrderState(OrderState.REJECTED);
					orderDTO.setRejectionReason(RejectionReason.INSUFFICIENT_CREDIT);
				}
			} else {
				orderDTO.setOrderState(OrderState.REJECTED);
				orderDTO.setRejectionReason(RejectionReason.UNKNOWN_CUSTOMER);
			}

			Thread.sleep(5000);
			customerPublisher.sendToOrderCallback(orderDTO);

			logger.info("Proccesed order id {}", orderDTO.getId());

		} catch (IOException | InterruptedException e) {
			logger.error(e.getMessage(), e);
		}

	}
}
