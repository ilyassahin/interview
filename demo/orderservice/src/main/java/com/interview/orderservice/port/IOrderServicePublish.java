package com.interview.orderservice.port;

import com.interview.orderservice.dto.OrderDTO;

public interface IOrderServicePublish {
    void sendOrder(OrderDTO orderDTO);
}
