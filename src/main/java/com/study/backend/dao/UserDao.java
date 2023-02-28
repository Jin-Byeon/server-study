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

import com.study.backend.dto.UserDto;
import com.study.backend.dto.UserResponse;

@Repository
public class UserDao implements IUserDao {
	private final JdbcTemplate jdbcTemplate;
	
	public UserDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public HashMap<String, UserResponse> registrate(HashMap<String, UserDto> user) {
		final String sql = "INSERT INTO users (email, token, username, password, bio) values (?, ?, ?, ?, ?)";
		
		int result = jdbcTemplate.update(sql, new PreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement preparedStatement) throws SQLException {
					preparedStatement.setString(1, user.get("user").getEmail());
					preparedStatement.setString(2, user.get("user").getToken());
					preparedStatement.setString(3, user.get("user").getUsername());
					preparedStatement.setString(4, user.get("user").getPassword());
					preparedStatement.setString(5, user.get("user").getBio());
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
		
		UserResponse result = jdbcTemplate.queryForObject(sql, new RowMapper<UserResponse>() {
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
		
		response.put("user", result);
		httpSession.setAttribute("Token", result.getToken());
		
		return response;
	}
	
	@Override
	public HashMap<String, UserResponse> getCurrentUser(HttpSession httpSession) {
		HashMap<String, UserResponse> response = new HashMap<>();
		final String sql = "SELECT email, token, username, bio, image FROM users WHERE token = ?";
		
		UserResponse result = jdbcTemplate.queryForObject(sql, new RowMapper<UserResponse>() {
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
		
		response.put("user", result);
		
		return response;
	}
	
	@Override
	public HashMap<String, UserResponse> updateUser(HashMap<String, UserDto> user, HttpSession httpSession) {
		UserDto updateUser = new UserDto();
		final String selectSql = "SELECT email, password, username, bio, image FROM users WHERE token = ?";
		final String updateSql = "UPDATE users SET email = ?, password = ?, username = ?, bio = ?, image = ? WHERE token = ?";
		
		UserDto selectResult = jdbcTemplate.queryForObject(selectSql, new RowMapper<UserDto>() {
			@Override
			public UserDto mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
				UserDto userDto = new UserDto();
				
				userDto.setEmail(resultSet.getString("email"));
				userDto.setPassword(resultSet.getString("password"));
				userDto.setUsername(resultSet.getString("username"));
				userDto.setBio(resultSet.getString("bio"));
				userDto.setImage(resultSet.getString("image"));
				
				return userDto;
			}
		}, httpSession.getAttribute("Token"));
		
		updateUser.setEmail(selectResult.getEmail());
		updateUser.setPassword(selectResult.getPassword());
		updateUser.setUsername(selectResult.getUsername());
		updateUser.setBio(selectResult.getBio());
		updateUser.setImage(selectResult.getImage());
		
		if (user.get("user").getEmail() != null) {
			updateUser.setEmail(user.get("user").getEmail());
		}
		if (user.get("user").getPassword() != null) {
			updateUser.setPassword(user.get("user").getPassword());
		}
		if (user.get("user").getUsername() != null) {
			updateUser.setUsername(user.get("user").getUsername());
		}
		if (user.get("user").getBio() != null) {
			updateUser.setBio(user.get("user").getBio());
		}
		if (user.get("user").getImage() != null) {
			updateUser.setImage(user.get("user").getImage());
		}
		
		int updateResult = jdbcTemplate.update(updateSql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setString(1, updateUser.getEmail());
				preparedStatement.setString(2, updateUser.getPassword());
				preparedStatement.setString(3, updateUser.getUsername());
				preparedStatement.setString(4, updateUser.getBio());
				preparedStatement.setString(5, updateUser.getImage());
				preparedStatement.setString(6, (String) httpSession.getAttribute("Token"));
			}
		});
		
		if (updateResult == 1) {
			HashMap<String, UserResponse> response = new HashMap<>();
			UserResponse userResponse = new UserResponse();
			
			userResponse.setEmail(updateUser.getEmail());
			userResponse.setToken((String) httpSession.getAttribute("Token"));
			userResponse.setUsername(updateUser.getUsername());
			userResponse.setBio(updateUser.getBio());
			userResponse.setImage(updateUser.getImage());
			response.put("user", userResponse);
			
			return response;
		}
		
		return null;
	}
}
