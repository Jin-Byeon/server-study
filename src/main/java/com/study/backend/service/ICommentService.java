package com.study.backend.service;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PathVariable;

import com.study.backend.dto.CommentDto;
import com.study.backend.dto.CommentResponse;

public interface ICommentService {
	HashMap<String, CommentResponse> addComment(String slug, HashMap<String, CommentDto> comment, HttpSession httpSession);
	HashMap<String, ArrayList<CommentResponse>> getComments(@PathVariable String slug, HttpSession httpSession);
}
