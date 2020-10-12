package com.interview.customerservice.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.interview.customerservice.entity.Customer;
import com.interview.customerservice.repository.CustomerRepository;

public class CustomerControllerTest {
	
	@InjectMocks
	CustomerController customerController;
	
	@Mock
	CustomerRepository customerRepository;

	private MockMvc mvc;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mvc = MockMvcBuilders.standaloneSetup(customerController).build();
	}
	
	@Test
	public void checkCreditLimitSuccess() throws Exception {
		
		String requestBodyJson = "{\"customerId\":1,\"amount\":20.0}";
		Customer customer = new Customer();
		customer.setCreditLimit(21);
		when(customerRepository.findById(1l)).thenReturn(Optional.of(customer));
		
		MvcResult result = mvc
				.perform(post("/customer/checkCreditLimit").content(requestBodyJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		
		MockHttpServletResponse response = result.getResponse();
		String responseMessage = response.getContentAsString();
		assertThat(responseMessage).isEqualTo("{\"state\":\"SUCCESS\"}");
		
	}
}

