package com.study.backend.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.study.backend.dto.UserDto;
import com.study.backend.dto.UserResponse;

@Repository
public class UserDao implements IUserDao {
	private JdbcTemplate jdbcTemplate;
	
	public UserDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public HashMap<String, UserResponse> registrate(HashMap<String, UserDto> user) {
		final String sql = "INSERT INTO users (email, token, username, password, bio) values (?, ?, ?, ?, ?)";
		
		int result = jdbcTemplate.update(sql, new PreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement pstmt) throws SQLException {
					pstmt.setString(1, user.get("user").getEmail());
					pstmt.setString(2, user.get("user").getToken());
					pstmt.setString(3, user.get("user").getUsername());
					pstmt.setString(4, user.get("user").getPassword());
					pstmt.setString(5, user.get("user").getBio());
				}
		});
		
		if (result == 1) {
			HashMap<String, UserResponse> response = new HashMap<>();
			UserResponse userResponse = new UserResponse();
			
			userResponse.setEmail(user.get("user").getEmail());
			userResponse.setToken(user.get("user").getToken());
			userResponse.setUsername(user.get("user").getUsername());
			userResponse.setBio(user.get("user").getBio());
			userResponse.setImage(user.get("user").getImage());
			response.put("user", userResponse);
			
			return response;
		}
		
		return null;
	}
	
	@Override
	public HashMap<String, UserResponse> authenticate(HashMap<String, UserDto> user, HttpSession httpSession) {
		HashMap<String, UserResponse> response = new HashMap<>();
		final String sql = "SELECT email, token, username, bio, image FROM users WHERE email = ? AND password = ?";
		
		List<UserResponse> users = jdbcTemplate.query(sql, new RowMapper<UserResponse>() {
			@Override
			public UserResponse mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
				UserResponse userResponse = new UserResponse();
				
				userResponse.setEmail(resultSet.getString("email"));
				userResponse.setToken(resultSet.getString("token"));
				userResponse.setUsername(resultSet.getString("username"));
				userResponse.setBio(resultSet.getString("bio"));
				userResponse.setImage(resultSet.getString("image"));
				
				return userResponse;
			}
		}, user.get("user").getEmail(), user.get("user").getPassword());
		
		response.put("user", users.get(0));
		httpSession.setAttribute("Token", users.get(0).getToken());
		
		return response;
	}
	
	@Override
	public HashMap<String, UserResponse> getCurrentUser(HttpSession httpSession) {
		HashMap<String, UserResponse> response = new HashMap<>();
		final String sql = "SELECT email, token, username, bio, image FROM users WHERE token = ?";
		
		List<UserResponse> users = jdbcTemplate.query(sql, new RowMapper<UserResponse>() {
			@Override
			public UserResponse mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
				UserResponse userResponse = new UserResponse();
				
				userResponse.setEmail(resultSet.getString("email"));
				userResponse.setToken(resultSet.getString("token"));
				userResponse.setUsername(resultSet.getString("username"));
				userResponse.setBio(resultSet.getString("bio"));
				userResponse.setImage(resultSet.getString("image"));
				
				return userResponse;
			}
		}, httpSession.getAttribute("Token"));
		
		response.put("user", users.get(0));
		
		return response;
	}
}
