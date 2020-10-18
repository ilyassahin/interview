package com.interview.customerservice.port;

import org.springframework.kafka.annotation.KafkaListener;

import com.interview.customerservice.constant.KafkaTopic;

public interface ICustomerMessaging{

    @KafkaListener(topics = KafkaTopic.TOPIC_DELIVERY)
    void consumeMessage(String content);
}
