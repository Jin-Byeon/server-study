package com.study.backend.service;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.study.backend.dao.ProfileDao;
import com.study.backend.dto.ProfileResponse;

@Service
public class ProfileService implements IProfileService {
	private final ProfileDao profileDao;
	
	public ProfileService(ProfileDao profileDao) {
		this.profileDao = profileDao;
	}
	
	@Override
	public HashMap<String, ProfileResponse> getProfile(String username, HttpSession httpSession) {
		return profileDao.getProfile(username, httpSession);
	}

	@Override
	public HashMap<String, ProfileResponse> followUser(String username, HttpSession httpSession) {
		return profileDao.followUser(username, httpSession);
	}
}
