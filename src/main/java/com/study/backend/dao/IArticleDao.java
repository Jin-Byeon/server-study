package com.study.backend.dao;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.study.backend.dto.ArticleDto;
import com.study.backend.dto.ArticleResponse;

public interface IArticleDao {
	HashMap<String, ArticleResponse> createArticle(HashMap<String, ArticleDto> article, HttpSession httpSession);
	HashMap<String, ArticleResponse> getArticle(String slug);
	HashMap<String, ArticleResponse> updateArticle(@PathVariable String slug, @RequestBody HashMap<String, ArticleDto> article, HttpSession httpSession);
	void deleteArticle(@PathVariable String slug, HttpSession httpSession);
}
