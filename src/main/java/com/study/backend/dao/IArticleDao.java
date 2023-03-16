package com.study.backend.dao;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import com.study.backend.dto.ArticleDto;
import com.study.backend.dto.ArticleResponse;

public interface IArticleDao {
	HashMap<String, ArticleResponse> createArticle(HashMap<String, ArticleDto> article, HttpSession httpSession);
}
