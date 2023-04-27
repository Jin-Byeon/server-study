package com.study.backend.dao;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestParam;

import com.study.backend.dto.ArticleDto;
import com.study.backend.dto.ArticleResponse;

public interface IArticleDao {
	HashMap<String, ArticleResponse> createArticle(HashMap<String, ArticleDto> article, HttpSession httpSession);
	HashMap<String, ArticleResponse> getArticle(String slug, HttpSession httpSession);
	HashMap<String, Object> listArticles(String tag, String author, String favorited, int limit, int offset, HttpSession httpSession);
	HashMap<String, ArticleResponse> updateArticle(String slug, HashMap<String, ArticleDto> article, HttpSession httpSession);
	void deleteArticle(String slug, HttpSession httpSession);
	HashMap<String, ArticleResponse> favoriteArticle(String slug, HttpSession httpSession);
	HashMap<String, ArticleResponse> unfavoriteArticle(String slug, HttpSession httpSession);
	HashMap<String, ArrayList<String>> getTags();
}
