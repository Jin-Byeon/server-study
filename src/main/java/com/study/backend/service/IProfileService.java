package com.study.backend.service;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import com.study.backend.dto.ProfileResponse;

public interface IProfileService {
	HashMap<String, ProfileResponse> getProfile(String username, HttpSession httpSession);
	HashMap<String, ProfileResponse> followUser(String username, HttpSession httpSession);
	HashMap<String, ProfileResponse> unfollowUser(String username, HttpSession httpSession);
}
