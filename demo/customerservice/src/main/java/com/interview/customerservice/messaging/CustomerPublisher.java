package com.interview.customerservice.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.customerservice.dto.OrderDTO;
import com.interview.customerservice.port.ICustomerPublisher;

@Service
public class CustomerPublisher implements ICustomerPublisher {

    private final static String TOPIC_ORDER_CALLBACK ="orderservicecallback";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KafkaTemplate kafkaTemplate;


    @Override
    public void sendToOrderCallback(OrderDTO orderDTO) throws JsonProcessingException {
        kafkaTemplate.send(TOPIC_ORDER_CALLBACK,objectMapper.writeValueAsString(orderDTO));
    }
}
