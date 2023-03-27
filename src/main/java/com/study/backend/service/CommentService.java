package com.study.backend.service;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.study.backend.dao.CommentDao;
import com.study.backend.dto.CommentDto;
import com.study.backend.dto.CommentResponse;

@Service
public class CommentService implements ICommentService {
	private final CommentDao commentDao;
	
	public CommentService(CommentDao commentDao) {
		this.commentDao = commentDao;
	}
	
	@Override
	public HashMap<String, CommentResponse> addComment(String slug, HashMap<String, CommentDto> comment, HttpSession httpSession) {
		return commentDao.addComment(slug, comment, httpSession);
	}
}
