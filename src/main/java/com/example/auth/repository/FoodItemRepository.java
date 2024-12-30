package com.example.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.auth.model.FoodItem;

public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {
}

