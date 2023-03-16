package com.study.backend.dto;

import javax.validation.constraints.Size;

public class AuthorDto {
	@Size(max = 100)
	private String username;
	@Size(max = 100)
	private String bio;
	@Size(max = 100)
	private String image;
	private boolean following;
	
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
	public boolean isFollowing() {
		return following;
	}
	public void setFollowing(boolean following) {
		this.following = following;
	}
}
