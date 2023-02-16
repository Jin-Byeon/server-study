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
	public HashMap<String, UserResponse> registration(HashMap<String, UserDto> user) {
		return userDao.registration(user);
	}
	
	@Override
	public HashMap<String, UserResponse> authentication(HashMap<String, UserDto> user, HttpSession httpSession) {
		return userDao.authentication(user, httpSession);
	}
}
