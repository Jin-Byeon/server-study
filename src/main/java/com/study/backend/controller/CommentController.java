package com.study.backend.controller;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.backend.dto.CommentDto;
import com.study.backend.dto.CommentResponse;
import com.study.backend.service.CommentService;

@RestController
@RequestMapping(value = "/api", produces = "application/json; charset=utf-8")
public class CommentController {
	private final CommentService commentService;
	
	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}

	@PostMapping("/articles/{slug}/comments")
	public HashMap<String, CommentResponse> addComment(@PathVariable String slug, @RequestBody HashMap<String, CommentDto> comment, HttpSession httpSession) {
		return commentService.addComment(slug, comment, httpSession);
	}
	
	@GetMapping("/articles/{slug}/comments")
	public HashMap<String, ArrayList<CommentResponse>> getComments(@PathVariable String slug, HttpSession httpSession) {
		return commentService.getComments(slug, httpSession);
	}
	
	@DeleteMapping("/articles/{slug}/comments/{id}")
	public void deleteComment(@PathVariable String slug, @PathVariable String id, HttpSession httpSession) {
		commentService.deleteComment(slug, Integer.parseInt(id), httpSession);
	}
}
