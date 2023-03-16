package com.study.backend.controller;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
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
}
