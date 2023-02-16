package com.study.backend.service;

import java.util.HashMap;

import com.study.backend.dto.UserDto;
import com.study.backend.dto.UserResponse;

public interface IUserService {
	HashMap<String, UserResponse> registration(HashMap<String, UserDto> user);
}
