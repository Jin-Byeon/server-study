package com.study.backend.service;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.study.backend.dao.ArticleDao;
import com.study.backend.dto.ArticleDto;
import com.study.backend.dto.ArticleResponse;

@Service
public class ArticleService implements IArticleService{
	private final ArticleDao articleDao;
	
	public ArticleService(ArticleDao articleDao) {
		this.articleDao = articleDao;
	}

	@Override
	public HashMap<String, ArticleResponse> createArticle(HashMap<String, ArticleDto> article, HttpSession httpSession) {
		return articleDao.createArticle(article, httpSession);
	}

	@Override
	public HashMap<String, ArticleResponse> getArticle(String slug, HttpSession httpSession) {
		return articleDao.getArticle(slug, httpSession);
	}
	
	@Override
	public HashMap<String, Object> listArticles(String tag, String author, String favorited, int limit, int offset, HttpSession httpSession) {
		return articleDao.listArticles(tag, author, favorited, limit, offset, httpSession);
	}
	
	@Override
	public HashMap<String, Object> feedArticles(int limit, int offset, HttpSession httpSession) {
		return articleDao.feedArticles(limit, offset, httpSession);
	}

	@Override
	public HashMap<String, ArticleResponse> updateArticle(String slug, HashMap<String, ArticleDto> article, HttpSession httpSession) {
		return articleDao.updateArticle(slug, article, httpSession);
	}

	@Override
	public void deleteArticle(String slug, HttpSession httpSession) {
		articleDao.deleteArticle(slug, httpSession);
	}
	
	@Override
	public HashMap<String, ArticleResponse> favoriteArticle(String slug, HttpSession httpSession) {
		return articleDao.favoriteArticle(slug, httpSession);
	}
	
	@Override
	public HashMap<String, ArticleResponse> unfavoriteArticle(String slug, HttpSession httpSession) {
		return articleDao.unfavoriteArticle(slug, httpSession);
	}

	@Override
	public HashMap<String, ArrayList<String>> getTags() {
		return articleDao.getTags();
	}
}
