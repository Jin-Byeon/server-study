package com.study.backend.dao;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import com.study.backend.dto.UserDto;
import com.study.backend.dto.UserResponse;

public interface IUserDao {
	HashMap<String, UserResponse> registrate(HashMap<String, UserDto> user);
	HashMap<String, UserResponse> authenticate(HashMap<String, UserDto> user, HttpSession httpSession);
	HashMap<String, UserResponse> getCurrentUser(HttpSession httpSession);
	HashMap<String, UserResponse> updateUser(HashMap<String, UserDto> user, HttpSession httpSession);
}
