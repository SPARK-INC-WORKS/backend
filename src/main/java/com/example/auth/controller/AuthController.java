package com.example.auth.controller;

import com.example.auth.dto.AuthRequest;
import com.example.auth.dto.AuthResponse;
import com.example.auth.dto.RegisterRequest;
import com.example.auth.dto.TokenAndOrders;
import com.example.auth.model.Order;
import com.example.auth.model.User;
import com.example.auth.repository.OrderRepository;
import com.example.auth.repository.UserRepository;
import com.example.auth.security.JwtUtil;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	@Autowired
    private  AuthenticationManager authenticationManager;
	@Autowired
    private  UserRepository userRepository;
	@Autowired
    private  PasswordEncoder passwordEncoder;
	@Autowired
    private  JwtUtil jwtUtil;
	@Autowired
	private OrderRepository orderRepository;

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
	    if (userRepository.existsByUsername(request.getUsername())) {
	        return ResponseEntity.badRequest().body(new AuthResponse(null, "Username already exists"));
	    }

	    if (userRepository.existsByEmail(request.getEmail())) {
	        return ResponseEntity.badRequest().body(new AuthResponse(null, "Email already exists"));
	    }

	    User user = new User();
	    user.setUsername(request.getUsername());
	    user.setPassword(passwordEncoder.encode(request.getPassword()));
	    user.setEmail(request.getEmail());
	    user.setRole(request.getRole());
	    userRepository.save(user);

	    // Convert role into a GrantedAuthority and pass it to generateToken
	    Collection<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole()));

		List<Order> userOrders = orderRepository.findByUserId(user.getId());

		String token = jwtUtil.generateToken(
				user.getId(),
				user.getUsername(),
				user.getEmail(),
//				userOrders,
				authorities
		);

//		return ResponseEntity.ok(new AuthResponse(token, "User registered successfully"));
		return ResponseEntity.ok(new TokenAndOrders(token, userOrders, "User registered successfully"));
	}


//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
//        try {
//            Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
//            );
//
//            String token = jwtUtil.generateToken(request.getUsername(),request.getRole());
//        	
//            return ResponseEntity.ok(new AuthResponse(token, "Login successful"));
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(new AuthResponse(null, "Invalid username or password"));
//        }
//    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            // After authentication, get the roles from the authenticated user
            String username = request.getUsername();
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
             User user = userRepository.findByUsername(username).orElse(null);
			List<Order> userOrders = orderRepository.findByUserId(user.getId());
            // Generate the JWT token with the username and roles
            String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getEmail(), authorities);

//            return ResponseEntity.ok(new AuthResponse(token, "Login successful"));
			return ResponseEntity.ok(new TokenAndOrders(token, userOrders, "Login successful"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new AuthResponse(null, "Invalid username or password"));
        }
    }

}
