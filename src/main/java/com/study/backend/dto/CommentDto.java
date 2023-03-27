package com.study.backend.dto;

import javax.validation.constraints.Size;

public class CommentDto {
	@Size(max = 100)
	private String body;

	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
}
