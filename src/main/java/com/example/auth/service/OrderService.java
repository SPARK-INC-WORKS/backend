package com.example.auth.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.auth.model.Order;
import com.example.auth.repository.OrderRepository;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public List<Order> getUserOrderHistory(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
