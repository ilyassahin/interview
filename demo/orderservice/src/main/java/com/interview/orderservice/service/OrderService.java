package com.interview.orderservice.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interview.orderservice.adapter.CustomerServiceAdapter;
import com.interview.orderservice.dto.OrderDTO;
import com.interview.orderservice.dto.SaveOrderRequestDTO;
import com.interview.orderservice.dto.SaveOrderResponseDTO;
import com.interview.orderservice.entity.Order;
import com.interview.orderservice.enums.OrderState;
import com.interview.orderservice.port.IOrderServicePublish;
import com.interview.orderservice.repository.OrderRepository;

@Service
public class OrderService {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	CustomerServiceAdapter customerServiceAdapter;

	@Autowired
	private IOrderServicePublish orderServicePublish;

	public SaveOrderResponseDTO saveOrder(SaveOrderRequestDTO orderDto) {

		Order order = modelMapper.map(orderDto, Order.class);
		order.setOrderState(OrderState.PENDING);
		orderRepository.save(order);
		orderServicePublish.sendOrder(modelMapper.map(order, OrderDTO.class));

		return modelMapper.map(order, SaveOrderResponseDTO.class);
	}

	public OrderDTO getOrderById(Long orderId) {
		Optional<Order> op = orderRepository.findById(orderId);
		return op.isPresent() ? modelMapper.map(op.get(), OrderDTO.class) : null;
	}

	public List<OrderDTO> getOrdersByCustomerId(Long customerId) {
		return Arrays.asList(modelMapper.map(orderRepository.findByCustomerId(customerId), OrderDTO[].class));
	}
}
