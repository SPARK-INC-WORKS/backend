package com.example.auth.dto;

import com.example.auth.model.Order;

import java.util.List;

public class TokenAndOrders {
    private String token;
    private List<Order> orders;
    private String message;

    public String getToken() {
        return token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public TokenAndOrders(String token, List<Order> orders, String message) {
        this.token = token;
        this.orders = orders;
        this.message = message;
    }
}
