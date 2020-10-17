package com.interview.orderservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.orderservice.dto.OrderDTO;
import com.interview.orderservice.entity.Order;
import com.interview.orderservice.port.IOrderMessaging;
import com.interview.orderservice.repository.OrderRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class OrderConsumer implements IOrderMessaging {

	private static final Logger logger = LoggerFactory.getLogger(OrderConsumer.class);

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private OrderRepository orderRepository;

	@Override
	public void callback(String message) {
		try {
			OrderDTO orderDTO = mapper.readValue(message, OrderDTO.class);
			Optional<Order> orderOptional = orderRepository.findById(orderDTO.getId());
			if (orderOptional.isPresent()) {
				Order order = orderOptional.get();
				order.setOrderState(orderDTO.getOrderState());
				order.setRejectionReason(orderDTO.getRejectionReason());
				orderRepository.save(order);
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}
}
