package com.interview.customerservice.port;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.interview.customerservice.dto.OrderDTO;

public interface ICustomerPublisher {

    void sendToOrderCallback(OrderDTO orderDTO) throws JsonProcessingException;
}
