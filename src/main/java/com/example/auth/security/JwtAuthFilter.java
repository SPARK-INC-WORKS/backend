package com.example.auth.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.auth.service.CustomUserDetailsService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
	
	
	@Autowired
    private  JwtUtil jwtUtil;
	@Autowired
    private  CustomUserDetailsService customUserDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	        throws ServletException, IOException {
	    try {
	        String jwt = parseJwt(request);
	        if (jwt != null) {
	            String username = jwtUtil.extractUsername(jwt);

	            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
	            
	            if (jwtUtil.validateToken(jwt, userDetails)) {
	                Claims claims = Jwts.parserBuilder()
	                        .setSigningKey(jwtUtil.getSigningKey())
	                        .build()
	                        .parseClaimsJws(jwt)
	                        .getBody();

	                @SuppressWarnings("unchecked")
	                List<String> roles = (List<String>) claims.get("roles");
	                Collection<GrantedAuthority> authorities = roles.stream()
	                        .map(SimpleGrantedAuthority::new)
	                        .collect(Collectors.toList()); // Use Collectors.toList() for explicit compatibility.

	                UsernamePasswordAuthenticationToken authentication = 
	                    new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
	                
	                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	                SecurityContextHolder.getContext().setAuthentication(authentication);
	            }
	        }
	    } catch (Exception e) {
	        logger.error("Cannot set user authentication: {}", e);
	    }

	    filterChain.doFilter(request, response);
	}



    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}
