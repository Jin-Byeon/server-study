package com.study.backend.controller;

import java.util.HashMap;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.backend.dto.ProfileResponse;
import com.study.backend.service.ProfileService;

@RestController
@RequestMapping(value = "/api", produces = "application/json; charset=utf-8")
public class ProfileController {
	private final ProfileService profileService;
	
	public ProfileController(ProfileService profileService) {
		this.profileService = profileService;
	}
	
	@GetMapping("/profiles/{username}")
	public HashMap<String, ProfileResponse> getProfile(@PathVariable String username) {
		return profileService.getProfile(username);
	}
	
}
