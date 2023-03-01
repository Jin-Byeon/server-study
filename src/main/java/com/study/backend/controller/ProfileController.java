package com.study.backend.controller;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
	public HashMap<String, ProfileResponse> getProfile(@PathVariable String username, HttpSession httpSession) {
		return profileService.getProfile(username, httpSession);
	}
	
	@PostMapping("/profiles/{username}/follow")
	public HashMap<String, ProfileResponse> followUser(@PathVariable String username, HttpSession httpSession) {
		return profileService.followUser(username, httpSession);
	}
	
	@DeleteMapping("/profiles/{username}/follow")
	public HashMap<String, ProfileResponse> unfollowUser(@PathVariable String username, HttpSession httpSession) {
		return profileService.unfollowUser(username, httpSession);
	}
}
