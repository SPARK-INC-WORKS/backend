package com.example.auth.model;

import com.example.auth.dto.OrderFoodItem;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;

import java.io.IOException;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String username;
    private Double totalPrice;
    private String email;
    private String status;

    @Lob
    private String foodItemsJson;

    @Transient
    private List<OrderFoodItem> foodItemList;

    // Constructors
    public Order() {}

    public Order(Long userId, String username, Double totalPrice, String email, String status, List<OrderFoodItem> foodItemList) {
        this.userId = userId;
        this.username = username;
        this.totalPrice = totalPrice;
        this.email = email;
        this.status = status;
        this.setFoodItemList(foodItemList);
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OrderFoodItem> getFoodItemList() {
        if (foodItemList == null && foodItemsJson != null) {
            this.foodItemList = convertJsonToFoodItemList(foodItemsJson);
        }
        return foodItemList;
    }

    public void setFoodItemList(List<OrderFoodItem> foodItemList) {
        this.foodItemList = foodItemList;
        this.foodItemsJson = convertFoodItemListToJson(foodItemList);
    }

    public String getFoodItemsJson() {
        return foodItemsJson;
    }

    public void setFoodItemsJson(String foodItemsJson) {
        this.foodItemsJson = foodItemsJson;
        this.foodItemList = convertJsonToFoodItemList(foodItemsJson);
    }

    // Utility methods for JSON conversion
    private String convertFoodItemListToJson(List<OrderFoodItem> foodItemList) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(foodItemList);
        } catch (JsonProcessingException e) {
            // Handle exception
            e.printStackTrace();
            return null;
        }
    }

    private List<OrderFoodItem> convertJsonToFoodItemList(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, OrderFoodItem.class));
        } catch (IOException e) {
            // Handle exception
            e.printStackTrace();
            return null;
        }
    }
}
