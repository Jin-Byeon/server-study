package com.study.backend.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
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
	public HashMap<String, UserResponse> registration(HashMap<String, UserDto> user) {
		
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
}
