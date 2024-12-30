package com.example.auth.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.auth.dto.ApiResponse;
import com.example.auth.dto.UserDetails;
import com.example.auth.model.*;
import com.example.auth.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import com.example.auth.service.ContactFormService;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
	private FoodItemRepository foodItemRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ReservationRepository reservationRepository;
	@Autowired
	private ContactFormRepository contactFormRepository;
	@Autowired
	 private ContactFormService contactFormService;
     @GetMapping("/{userId}")
	 @PreAuthorize("hasRole('ROLE_USER')")
	 public ResponseEntity<?> getUserDetails(@PathVariable Long userId) {
		 User user = userRepository.findById(userId).orElse(null);
		 if (user == null) {
			 return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<String>("User not found", null));
		 }
		 List<Order> userOrders = orderRepository.findByUserId(userId);
		 List<String> roles =new ArrayList<>();
		 roles.add(user.getRole());
       return ResponseEntity.ok(new UserDetails(user.getUsername(),user.getEmail(),roles,user.getId(),userOrders));

	 }
	  @PreAuthorize("hasRole('ROLE_USER')")
	  @GetMapping("/get-food-items")
	  public ResponseEntity<?> getAllFoodItems() {
		  return ResponseEntity.ok(new ApiResponse<List<FoodItem>>("Food items retrieved successfully", foodItemRepository.findAll()));
	  }
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostMapping("/create-reservation")
	public ResponseEntity<?> createReservation(@RequestBody Reservation reservation) {
		// Save the reservation to the database
		Reservation savedReservation = reservationRepository.save(reservation);
		// Return a response with the created reservation
		return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<Reservation>("Reservation created successfully", savedReservation));
	}


	@PreAuthorize("hasRole('ROLE_USER')")
	  @PostMapping("/contact-form")
    public ResponseEntity<?> submitContactForm(@RequestBody ContactForm contactForm) {
        ContactForm savedContactForm = contactFormRepository.save(contactForm);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<ContactForm>("Contact form submitted successfully", savedContactForm));
    }
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostMapping("/place-order")
	public ResponseEntity<?> placeOrder(@RequestBody Order order)  {
		// Set the initial status of the order
		order.setStatus("preparing");

		// Save the order to the database
		Order savedOrder = orderRepository.save(order);

		// Return a response with the created order
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponse<Order>("Order placed successfully", savedOrder));

	}


	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/get-orders/{userId}")
	public ResponseEntity<?> getOrdersByUserId(@PathVariable Long userId) {
		// Retrieve orders for the specified user
		List<Order> userOrders = orderRepository.findByUserId(userId);

		// Return the list of orders
		return ResponseEntity.ok(new ApiResponse<List<Order>>("Orders retrieved successfully", userOrders));
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@PutMapping("/cancel-order/{id}")
	public ResponseEntity<?> cancelOrder(@PathVariable Long id) {
		Optional<Order> order = orderRepository.findById(id);

		if (order.isPresent()) {
			order.get().setStatus("cancelled");
			Order updatedOrder = orderRepository.save(order.get());
			return ResponseEntity.ok(new ApiResponse<Order>("Order cancelled successfully", updatedOrder));
		} else {
			return ResponseEntity.status(404).body(new ApiResponse<String>("Order not found", null));
		}
	}

   
}

