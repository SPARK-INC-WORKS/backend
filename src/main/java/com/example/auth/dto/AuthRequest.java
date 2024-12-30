package com.example.auth.dto;

import lombok.Data;


@Data

public class AuthRequest {
public AuthRequest(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
private String username;
public String getUsername() {
	return username;
}
public void setUsername(String username) {
	this.username = username;
}
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}


private String password;
}
