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
import com.interview.orderservice.constant.State;
import com.interview.orderservice.dto.CheckCreditLimitRequestDTO;
import com.interview.orderservice.dto.CheckCreditLimitResponseDTO;
import com.interview.orderservice.dto.SaveOrderRequestDTO;
import com.interview.orderservice.dto.SaveOrderResponseDTO;
import com.interview.orderservice.entity.Orderr;
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

		Orderr order = new Orderr();
		order.setState(State.PENDING);
		order.setAmount(orderDto.getAmount());
		order.setCustomerId(orderDto.getCustomerId());
		orderRepository.save(order);

		CheckCreditLimitRequestDTO requestDTO = new CheckCreditLimitRequestDTO();
		requestDTO.setCustomerId(order.getCustomerId());
		requestDTO.setAmount(order.getAmount());
		CheckCreditLimitResponseDTO creditLimitResponseDTO = customerServiceAdapter.checkCreditLimit(requestDTO);

		if (State.SUCCESS.equals(creditLimitResponseDTO.getState())) {
			order.setState(State.APPROVED);
			orderRepository.save(order);
			responseDTO.setState(order.getState());
		} else {
			order.setState(State.REJECTED);
			order.setRejectionReason(creditLimitResponseDTO.getDetail());
			responseDTO.setState(order.getState());
			responseDTO.setRejectionReason(order.getRejectionReason());
		}

		return responseDTO;
	}

	@GetMapping("/getOrderById")
	public Orderr getOrderById(@RequestParam Long orderId) {
		Optional<Orderr> op = orderRepository.findById(orderId);
		return op.isPresent() ? op.get() : null;
	}

	@GetMapping("/getOrderByCustomerId")
	public List<Orderr> getOrdersByCustomerId(@RequestParam Long customerId) {
		return orderRepository.findByCustomerId(customerId);
	}
}
