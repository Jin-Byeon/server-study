package com.study.backend.bean;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
public class UserController {
	@GetMapping("/")
	public String authenticationHeader(@RequestHeader("Authorization") String token) {
		System.out.println(token);
		return token;
	}
}
