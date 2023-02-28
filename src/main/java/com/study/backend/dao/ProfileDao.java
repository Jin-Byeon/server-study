package com.study.backend.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.study.backend.dto.ProfileResponse;

@Repository
public class ProfileDao implements IProfileDao {
	private final JdbcTemplate jdbcTemplate;
	
	public ProfileDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public HashMap<String, ProfileResponse> getProfile(String username) {
		HashMap<String, ProfileResponse> response = new HashMap<>();
		final String sql = "SELECT username, bio, image, following FROM users WHERE username = ?";
		
		ProfileResponse result = jdbcTemplate.queryForObject(sql, new RowMapper<ProfileResponse>() {
			@Override
			public ProfileResponse mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
				ProfileResponse profileResponse = new ProfileResponse();

				profileResponse.setUsername(resultSet.getString("username"));
				profileResponse.setBio(resultSet.getString("bio"));
				profileResponse.setImage(resultSet.getString("image"));
				
				if (resultSet.getInt("following") == 0) {
					profileResponse.setFollowing(false);
					
					return profileResponse;
				} else  {
					profileResponse.setFollowing(true);
					
					return profileResponse;
				}
			}
		}, username);
		
		response.put("profile", result);
		
		return response;
	}
}
