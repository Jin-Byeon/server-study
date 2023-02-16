package com.study.backend.dao;

import java.util.HashMap;

import com.study.backend.dto.UserDto;
import com.study.backend.dto.UserResponse;

public interface IUserDao {
	HashMap<String, UserResponse> registration(HashMap<String, UserDto> user);
}
