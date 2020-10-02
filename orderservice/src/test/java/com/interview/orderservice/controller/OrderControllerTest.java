package com.interview.orderservice.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.interview.orderservice.adapter.CustomerServiceAdapter;
import com.interview.orderservice.constant.Detail;
import com.interview.orderservice.constant.State;
import com.interview.orderservice.dto.CheckCreditLimitResponseDTO;
import com.interview.orderservice.repository.OrderRepository;

public class OrderControllerTest {
	
	@InjectMocks
	private OrderController orderController;
	
	@Mock
	private CustomerServiceAdapter customerServiceAdapter;
	
	@Mock
	private OrderRepository orderRepository;
	
	private MockMvc mvc;

	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mvc = MockMvcBuilders.standaloneSetup(orderController).build();
	}
	
	@Test
	public void saveOrderApproved() throws Exception {
		
		String requestBodyJson = "{\"customerId\":1,\"amount\":20.0}";
		
		CheckCreditLimitResponseDTO checkCreditLimitResponseDTO = new CheckCreditLimitResponseDTO();
		checkCreditLimitResponseDTO.setState(State.SUCCESS);
		when(customerServiceAdapter.checkCreditLimit(Mockito.any())).thenReturn(checkCreditLimitResponseDTO);
		
		MvcResult result = mvc
				.perform(post("/order/save").content(requestBodyJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		
		MockHttpServletResponse response = result.getResponse();
		String responseMessage = response.getContentAsString();
		
		assertThat(responseMessage).isEqualTo("{\"state\":\"APPROVED\"}");

	}
	
	@Test
	public void saveOrderRejected() throws Exception {
		
		String requestBodyJson = "{\"customerId\":1,\"amount\":20.0}";
		
		CheckCreditLimitResponseDTO checkCreditLimitResponseDTO = new CheckCreditLimitResponseDTO();
		checkCreditLimitResponseDTO.setState(State.FAIL);
		checkCreditLimitResponseDTO.setDetail(Detail.INSUFFICIENT_CREDIT);
		when(customerServiceAdapter.checkCreditLimit(Mockito.any())).thenReturn(checkCreditLimitResponseDTO);
		
		MvcResult result = mvc
				.perform(post("/order/save").content(requestBodyJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		
		MockHttpServletResponse response = result.getResponse();
		String responseMessage = response.getContentAsString();
		
		assertThat(responseMessage).isEqualTo("{\"state\":\"REJECTED\",\"rejectionReason\":\"INSUFFICIENT_CREDIT\"}");

	}
		
}
