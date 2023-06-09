package com.study.backend.dao;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import com.study.backend.dto.UserDto;
import com.study.backend.dto.UserResponse;

public interface IUserDao {
	public HashMap<String, UserResponse> registrate(HashMap<String, UserDto> user);
	public HashMap<String, UserResponse> authenticate(HashMap<String, UserDto> user, HttpSession httpSession);
	public HashMap<String, UserResponse> getCurrentUser(HttpSession httpSession);
	public HashMap<String, UserResponse> updateUser(HashMap<String, UserDto> user, HttpSession httpSession);
}
