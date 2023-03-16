package com.study.backend.service;

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

}