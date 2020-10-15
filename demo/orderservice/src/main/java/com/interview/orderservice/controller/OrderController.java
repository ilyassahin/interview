package com.interview.orderservice.controller;

import java.util.List;

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
import com.interview.orderservice.dto.OrderDTO;
import com.interview.orderservice.dto.SaveOrderRequestDTO;
import com.interview.orderservice.dto.SaveOrderResponseDTO;
import com.interview.orderservice.repository.OrderRepository;
import com.interview.orderservice.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {
	
	@Autowired
	OrderRepository orderRepository;

	@Autowired
	CustomerServiceAdapter customerServiceAdapter;
	
	@Autowired
	OrderService orderService;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@PostMapping("/save")
	@Transactional(rollbackOn = RestClientException.class)
	public SaveOrderResponseDTO saveOrder(@RequestBody SaveOrderRequestDTO orderDto) {
		log.info("save order consumed!");
		return orderService.saveOrder(orderDto);
	}

	@GetMapping("/getOrderById")
	public OrderDTO getOrderById(@RequestParam Long orderId) {
		return orderService.getOrderById(orderId);
	}

	@GetMapping("/getOrderByCustomerId")
	public List<OrderDTO> getOrdersByCustomerId(@RequestParam Long customerId) {
		return orderService.getOrdersByCustomerId(customerId);
	}
}
