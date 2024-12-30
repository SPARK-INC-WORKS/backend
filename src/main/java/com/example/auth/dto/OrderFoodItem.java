package com.example.auth.dto;

import lombok.Data;

@Data
public class OrderFoodItem {
    private Long id;
    private String name;
    private String description;
    private String category;
    private Double price;
    private String image;
    private Integer quantity;

    public OrderFoodItem() {
    }

    public OrderFoodItem(String name, String description, String category, Double price, String image, Integer quantity) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.image = image;
        this.quantity = quantity;
    }

    public OrderFoodItem(String name, String description, String category, Double price, String image, Integer quantity, Long id) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.image = image;
        this.quantity = quantity;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
