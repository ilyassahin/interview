package com.interview.customerservice.port;

import org.springframework.kafka.annotation.KafkaListener;

public interface ICustomerMessaging{

    public static final String TOPIC_DELIVERY="orderservice";

    @KafkaListener(topics = TOPIC_DELIVERY)
    void consumeMessage(String content);
}
