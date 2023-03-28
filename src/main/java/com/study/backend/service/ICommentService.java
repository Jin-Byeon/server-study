package com.study.backend.service;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import com.study.backend.dto.CommentDto;
import com.study.backend.dto.CommentResponse;

public interface ICommentService {
	HashMap<String, CommentResponse> addComment(String slug, HashMap<String, CommentDto> comment, HttpSession httpSession);
	HashMap<String, ArrayList<CommentResponse>> getComments(String slug, HttpSession httpSession);
	void deleteComment(String slug, int id, HttpSession httpSession);
}
