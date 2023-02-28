package com.study.backend.service;

import java.util.HashMap;

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
	public HashMap<String, ProfileResponse> getProfile(String username) {
		return profileDao.getProfile(username);
	}
	
}
