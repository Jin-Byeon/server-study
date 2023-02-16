package com.study.backend.service;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.study.backend.dao.UserDao;
import com.study.backend.dto.UserDto;
import com.study.backend.dto.UserResponse;

@Service
public class UserService implements IUserService {
	private UserDao userDao;
	
	public UserService(UserDao userDao) {
		this.userDao = userDao;
	}
	
	@Override
	public HashMap<String, UserResponse> registrate(HashMap<String, UserDto> user) {
		return userDao.registrate(user);
	}
	
	@Override
	public HashMap<String, UserResponse> authenticate(HashMap<String, UserDto> user, HttpSession httpSession) {
		return userDao.authenticate(user, httpSession);
	}
	
	@Override
	public HashMap<String, UserResponse> getCurrentUser(HttpSession httpSession) {
		return userDao.getCurrentUser(httpSession);
	}
	
	@Override
	public HashMap<String, UserResponse> updateUser(HashMap<String, UserDto> user, HttpSession httpSession) {
		return userDao.updateUser(user, httpSession);
	}
}
