package com.interview.orderservice.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestClientException;

import com.interview.orderservice.adapter.CustomerServiceAdapter;
import com.interview.orderservice.dto.CheckCreditLimitRequestDTO;
import com.interview.orderservice.dto.CheckCreditLimitResponseDTO;
import com.interview.orderservice.dto.OrderDTO;
import com.interview.orderservice.dto.SaveOrderRequestDTO;
import com.interview.orderservice.dto.SaveOrderResponseDTO;
import com.interview.orderservice.entity.Order;
import com.interview.orderservice.enums.OrderState;
import com.interview.orderservice.enums.State;
import com.interview.orderservice.repository.OrderRepository;

@Service
public class OrderService {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	CustomerServiceAdapter customerServiceAdapter;
	
	@Transactional(rollbackOn = RestClientException.class)
	public SaveOrderResponseDTO saveOrder(@RequestBody SaveOrderRequestDTO orderDto) {
		SaveOrderResponseDTO responseDTO = new SaveOrderResponseDTO();

		Order order = modelMapper.map(orderDto,Order.class);
		order.setOrderState(OrderState.PENDING);
		orderRepository.save(order);

		CheckCreditLimitRequestDTO requestDTO = modelMapper.map(order,CheckCreditLimitRequestDTO.class);
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
	
	public OrderDTO getOrderById(Long orderId) {
		Optional<Order> op = orderRepository.findById(orderId);
		return op.isPresent() ? modelMapper.map(op.get(), OrderDTO.class): null;
	}
	
	public List<OrderDTO> getOrdersByCustomerId(Long customerId) {
		return Arrays.asList(modelMapper.map(orderRepository.findByCustomerId(customerId), OrderDTO[].class));
	}
}
