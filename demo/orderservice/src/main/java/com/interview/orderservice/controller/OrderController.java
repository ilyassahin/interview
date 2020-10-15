package com.interview.orderservice.controller;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import com.interview.orderservice.adapter.CustomerServiceAdapter;
import com.interview.orderservice.dto.CheckCreditLimitRequestDTO;
import com.interview.orderservice.dto.CheckCreditLimitResponseDTO;
import com.interview.orderservice.dto.SaveOrderRequestDTO;
import com.interview.orderservice.dto.SaveOrderResponseDTO;
import com.interview.orderservice.entity.Order;
import com.interview.orderservice.enums.OrderState;
import com.interview.orderservice.enums.State;
import com.interview.orderservice.repository.OrderRepository;

@RestController
@RequestMapping("/order")
public class OrderController {

	
	
	@Autowired
	OrderRepository orderRepository;

	@Autowired
	CustomerServiceAdapter customerServiceAdapter;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@PostMapping("/save")
	@Transactional(rollbackOn = RestClientException.class)
	public SaveOrderResponseDTO saveOrder(@RequestBody SaveOrderRequestDTO orderDto) {

		log.info("save order consumed!");
		SaveOrderResponseDTO responseDTO = new SaveOrderResponseDTO();

		Order order = new Order();
		order.setOrderState(OrderState.PENDING);
		order.setAmount(orderDto.getAmount());
		order.setCustomerId(orderDto.getCustomerId());
		orderRepository.save(order);

		CheckCreditLimitRequestDTO requestDTO = new CheckCreditLimitRequestDTO();
		requestDTO.setCustomerId(order.getCustomerId());
		requestDTO.setAmount(order.getAmount());
		CheckCreditLimitResponseDTO creditLimitResponseDTO = customerServiceAdapter.checkCreditLimit(requestDTO);

		if (State.SUCCESS.equals(creditLimitResponseDTO.getState())) {
			order.setOrderState(OrderState.APPROVED);
			orderRepository.save(order);
			responseDTO.setOrderState(order.getOrderState());
		} else {
			order.setOrderState(OrderState.REJECTED);
			order.setRejectionReason(creditLimitResponseDTO.getRejectionReason());
			responseDTO.setOrderState(order.getOrderState());
			responseDTO.setRejectionReason(order.getRejectionReason());
		}

		return responseDTO;
	}

	@GetMapping("/getOrderById")
	public Order getOrderById(@RequestParam Long orderId) {
		Optional<Order> op = orderRepository.findById(orderId);
		return op.isPresent() ? op.get() : null;
	}

	@GetMapping("/getOrderByCustomerId")
	public List<Order> getOrdersByCustomerId(@RequestParam Long customerId) {
		return orderRepository.findByCustomerId(customerId);
	}
}
