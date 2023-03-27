package com.study.backend.dto;

import javax.validation.constraints.Size;

public class CommentResponse {
	private int id;
	@Size(max = 100)
	private String createdAt;
	@Size(max = 100)
	private String updatedAt;
	@Size(max = 100)
	private String body;
	private AuthorDto author;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public String getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public AuthorDto getAuthor() {
		return author;
	}
	public void setAuthor(AuthorDto author) {
		this.author = author;
	}
}
