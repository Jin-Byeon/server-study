package com.study.backend.service;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.study.backend.dto.ArticleDto;
import com.study.backend.dto.ArticleResponse;

public interface IArticleService {
	HashMap<String, ArticleResponse> createArticle(HashMap<String, ArticleDto> article, HttpSession httpSession);
	HashMap<String, ArticleResponse> getArticle(String slug, HttpSession httpSession);
	HashMap<String, ArticleResponse> updateArticle(String slug, HashMap<String, ArticleDto> article, HttpSession httpSession);
	void deleteArticle(@PathVariable String slug, HttpSession httpSession);
	HashMap<String, ArticleResponse> favoriteArticle(String slug, HttpSession httpSession);
}
