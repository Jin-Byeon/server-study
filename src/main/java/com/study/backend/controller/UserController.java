package com.study.backend.controller;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.backend.dto.UserDto;
import com.study.backend.dto.UserResponse;
import com.study.backend.service.UserService;

@RestController
@RequestMapping(value = "/api", produces = "application/json; charset=utf-8")
public class UserController {
	private UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping("/users")
	public HashMap<String, UserResponse> registration(@RequestHeader("Authorization") String token, @RequestBody HashMap<String, UserDto> user) {
		user.get("user").setToken(token);
		user.get("user").setBio("I work at statefarm");
		
		return userService.registration(user);
	}
	
	@PostMapping("/users/login")
	public HashMap<String, UserResponse> authentication(@RequestBody HashMap<String, UserDto> user, HttpSession httpSession) {
		return userService.authentication(user, httpSession);
	}
}