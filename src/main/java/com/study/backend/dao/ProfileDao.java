package com.study.backend.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.study.backend.dto.ProfileResponse;

@Repository
public class ProfileDao implements IProfileDao {
	private final JdbcTemplate jdbcTemplate;
	private final String getCurrentUsernameSql = "SELECT username FROM users WHERE token = ?";
	private final String followAndUnfollowSql = "MERGE INTO follow USING dual ON (username = ? AND follow_username = ?) WHEN MATCHED THEN UPDATE SET following = ? WHEN NOT MATCHED THEN INSERT (username, follow_username, following) VALUES (?, ? ,?)";
	
	public ProfileDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public String getCurrentUsername(HttpSession httpSession) {
		return jdbcTemplate.queryForObject(getCurrentUsernameSql, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
				return resultSet.getString("username");
			}
		}, httpSession.getAttribute("Token"));
	}

	@Override
	public HashMap<String, ProfileResponse> getProfile(String username, HttpSession httpSession) {
		HashMap<String, ProfileResponse> response = new HashMap<>();
		ProfileResponse selectResult = new ProfileResponse();
		final String selectSql = "SELECT username, bio, image FROM users WHERE username = ?";
		final String selectJoinSql = "SELECT users.username, users.bio, users.image, follow.following FROM users LEFT JOIN follow ON users.username = follow.follow_username AND follow.username = ? WHERE users.username = ?";
		
		if (httpSession.getAttribute("Token") == null) {
			selectResult = jdbcTemplate.queryForObject(selectSql, new RowMapper<ProfileResponse>() {
				@Override
				public ProfileResponse mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
					ProfileResponse profileResponse = new ProfileResponse();

					profileResponse.setUsername(resultSet.getString("username"));
					profileResponse.setBio(resultSet.getString("bio"));
					profileResponse.setImage(resultSet.getString("image"));
					profileResponse.setFollowing(false);
						
					return profileResponse;
				}
			}, username);
		}
		
		if (httpSession.getAttribute("Token") != null) {
			String currentUsername = getCurrentUsername(httpSession);
			
			selectResult = jdbcTemplate.queryForObject(selectJoinSql, new RowMapper<ProfileResponse>() {
				@Override
				public ProfileResponse mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
					ProfileResponse profileResponse = new ProfileResponse();

					profileResponse.setUsername(resultSet.getString("username"));
					profileResponse.setBio(resultSet.getString("bio"));
					profileResponse.setImage(resultSet.getString("image"));
					
					if (resultSet.getInt("following") == 1) {
						profileResponse.setFollowing(true);
						
						return profileResponse;
					}

					profileResponse.setFollowing(false);
						
					return profileResponse;
				}
			}, currentUsername, username);
		}
		
		response.put("profile", selectResult);
		
		return response;
	}
	
	@Override
	public HashMap<String, ProfileResponse> followUser(String username, HttpSession httpSession) {
		String currentUsername = getCurrentUsername(httpSession);
		
		int updateResult = jdbcTemplate.update(followAndUnfollowSql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setString(1, currentUsername);
				preparedStatement.setString(2, username);
				preparedStatement.setInt(3, 1);
				preparedStatement.setString(4, currentUsername);
				preparedStatement.setString(5, username);
				preparedStatement.setInt(6, 1);	
			}
		});
		
		if (updateResult == 1) {
			return getProfile(username, httpSession);
		}
		
		return null;
	}
	
	@Override
	public HashMap<String, ProfileResponse> unfollowUser(String username, HttpSession httpSession) {
		String currentUsername = getCurrentUsername(httpSession);
		
		int updateResult = jdbcTemplate.update(followAndUnfollowSql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setString(1, currentUsername);
				preparedStatement.setString(2, username);
				preparedStatement.setInt(3, 0);
				preparedStatement.setString(4, currentUsername);
				preparedStatement.setString(5, username);
				preparedStatement.setInt(6, 0);	
			}
		});
		
		if (updateResult == 1) {
			return getProfile(username, httpSession);
		}
		
		return null;
	}
}
