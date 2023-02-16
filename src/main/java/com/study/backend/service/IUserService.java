package com.study.backend.service;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import com.study.backend.dto.UserDto;
import com.study.backend.dto.UserResponse;

public interface IUserService {
	HashMap<String, UserResponse> registration(HashMap<String, UserDto> user);
	HashMap<String, UserResponse> authentication(HashMap<String, UserDto> user, HttpSession httpSession);
}
