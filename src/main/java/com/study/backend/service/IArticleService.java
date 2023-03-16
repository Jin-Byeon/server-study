package com.study.backend.service;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import com.study.backend.dto.ArticleDto;
import com.study.backend.dto.ArticleResponse;

public interface IArticleService {
	HashMap<String, ArticleResponse> createArticle(HashMap<String, ArticleDto> article, HttpSession httpSession);
}
