package com.study.backend.dto;

import javax.validation.constraints.Size;

public class UserResponse {
	@Size(max = 100)
	private String email;
	@Size(max = 100)
	private String token;
	@Size(max = 100)
	private String username;
	@Size(max = 100)
	private String bio;
	@Size(max = 100)
	private String image;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getBio() {
		return bio;
	}
	public void setBio(String bio) {
		this.bio = bio;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
}
