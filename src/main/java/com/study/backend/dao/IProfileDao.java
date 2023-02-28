package com.study.backend.dao;

import java.util.HashMap;

import com.study.backend.dto.ProfileResponse;

public interface IProfileDao {
	public HashMap<String, ProfileResponse> getProfile(String username);
}
