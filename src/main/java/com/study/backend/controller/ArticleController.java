package com.study.backend.controller;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.backend.dto.ArticleDto;
import com.study.backend.dto.ArticleResponse;
import com.study.backend.service.ArticleService;

@RestController
@RequestMapping(value = "/api", produces = "application/json; charset=utf-8")
public class ArticleController {
	private final ArticleService articleService;
	
	public ArticleController(ArticleService articleService) {
		this.articleService = articleService;
	}
	
	@PostMapping("/articles")
	public HashMap<String, ArticleResponse> createArticle(@RequestBody HashMap<String, ArticleDto> article, HttpSession httpSession) {
		return articleService.createArticle(article, httpSession);
	}
	
	@GetMapping("/articles/{slug}")
	public HashMap<String, ArticleResponse> getArticle(@PathVariable String slug, HttpSession httpSession) {
		return articleService.getArticle(slug, httpSession);
	}
	
	@PutMapping("/articles/{slug}")
	public HashMap<String, ArticleResponse> updateArticle(@PathVariable String slug, @RequestBody HashMap<String, ArticleDto> article, HttpSession httpSession) {
		return articleService.updateArticle(slug, article, httpSession);
	}
	
	@DeleteMapping("/articles/{slug}")
	public void deleteArticle(@PathVariable String slug, HttpSession httpSession) {
		articleService.deleteArticle(slug, httpSession);
	}
	
	@PostMapping("/articles/{slug}/favorite")
	public HashMap<String, ArticleResponse> favoriteArticle(@PathVariable String slug, HttpSession httpSession) {
		return articleService.favoriteArticle(slug, httpSession);
	}
}
