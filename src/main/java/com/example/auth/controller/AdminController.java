package com.example.auth.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.auth.model.ContactForm;
import com.example.auth.model.FoodItem;
import com.example.auth.model.Order;
import com.example.auth.model.Reservation;
import com.example.auth.repository.FoodItemRepository;
import com.example.auth.repository.OrderRepository;
import com.example.auth.service.ContactFormService;
import com.example.auth.service.ReservationService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

	@Autowired
    private OrderRepository orderRepository;

	 @Autowired
	    private FoodItemRepository foodItemRepository;


	@Autowired
	private ReservationService reservationService;

    @Autowired
	 private ContactFormService contactFormService;


    @PreAuthorize("hasRole('ROLE_ADMIN')")
	 @GetMapping("/dashboard")
	    public String getAdminDashboard() {
	        return "Welcome to the Admin Dashboard!";
	    }


	    // Admin: Get all orders
	    @PreAuthorize("hasRole('ROLE_ADMIN')")
	    @GetMapping("/orders")
	    public ResponseEntity<?> getAllOrders() {
	        List<Order> orders = orderRepository.findAll();

	        if (orders.isEmpty()) {
	            return ResponseEntity.status(404).body("No orders found");
	        }

	        return ResponseEntity.ok(orders);
	    }


	// Admin: Change order status
	 @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        Optional<Order> order = orderRepository.findById(id);

        if (order.isPresent()) {
            order.get().setStatus(status);
            Order updatedOrder = orderRepository.save(order.get());
            return ResponseEntity.ok(updatedOrder);
        } else {
            return ResponseEntity.status(404).body("Order not found");
        }
    }

	 @PreAuthorize("hasRole('ROLE_ADMIN')")
    // Admin: Add new food item
    @PostMapping
    public FoodItem addFoodItem(@RequestBody FoodItem foodItem) {
        return foodItemRepository.save(foodItem);
    }

	 @PreAuthorize("hasRole('ROLE_ADMIN')")
    // Admin: Update food item
    @PutMapping("/{id}")
    public FoodItem updateFoodItem(@PathVariable Long id, @RequestBody FoodItem updatedFoodItem) {
        return foodItemRepository.findById(id).map(foodItem -> {
            foodItem.setName(updatedFoodItem.getName());
            foodItem.setPrice(updatedFoodItem.getPrice());
            foodItem.setDescription(updatedFoodItem.getDescription());
            foodItem.setCategory(updatedFoodItem.getCategory());
            foodItem.setImage(updatedFoodItem.getImage());
            return foodItemRepository.save(foodItem);
        }).orElseThrow(() -> new RuntimeException("Food item not found"));
    }

     @GetMapping("/get-all-reservations")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }


     @GetMapping("/get-contact-forms")
    public ResponseEntity<List<ContactForm>> getAllContactForms() {
        List<ContactForm> contactForms = contactFormService.getAllContactForms();
        return ResponseEntity.ok(contactForms);
    }
}

