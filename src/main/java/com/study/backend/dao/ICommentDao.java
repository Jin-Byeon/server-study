package com.study.backend.dao;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import com.study.backend.dto.CommentDto;
import com.study.backend.dto.CommentResponse;

public interface ICommentDao {
	HashMap<String, CommentResponse> addComment(String slug, HashMap<String, CommentDto> comment, HttpSession httpSession);
}
