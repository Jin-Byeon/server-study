package com.study.backend.dto;

import java.util.ArrayList;

import javax.validation.constraints.Size;

public class ArticleDto {
	@Size(max = 100)
	private String title;
	@Size(max = 100)
	private String description;
	@Size(max = 100)
	private String body;
	private ArrayList<String> tagList;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public ArrayList<String> getTagList() {
		return tagList;
	}
	public void setTagList(ArrayList<String> tagList) {
		this.tagList = tagList;
	}
}
