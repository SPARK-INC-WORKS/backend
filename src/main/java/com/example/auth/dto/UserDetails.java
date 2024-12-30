package com.example.auth.dto;

import com.example.auth.model.Order;

import java.util.List;

public class UserDetails {
    private String username;
    private String email;
    private List<String> roles;
    private Long id;
    private  List<Order>  orders;

    public UserDetails() {
    }

    public UserDetails(String username, String email, List<String> roles, Long id, List<Order> orders) {
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.id = id;
        this.orders = orders;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
