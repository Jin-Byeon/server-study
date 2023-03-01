package com.study.backend.service;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import com.study.backend.dto.ProfileResponse;

public interface IProfileService {
	public HashMap<String, ProfileResponse> getProfile(String username, HttpSession httpSession);
	public HashMap<String, ProfileResponse> followUser(String username, HttpSession httpSession);
}
