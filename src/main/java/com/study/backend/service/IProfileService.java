package com.study.backend.service;

import java.util.HashMap;

import com.study.backend.dto.ProfileResponse;

public interface IProfileService {
	public HashMap<String, ProfileResponse> getProfile(String username);
}
