package com.study.backend.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.study.backend.dto.AuthorDto;
import com.study.backend.dto.CommentDto;
import com.study.backend.dto.CommentResponse;
import com.study.backend.dto.UserDto;

@Repository
public class CommentDao implements ICommentDao {
	private JdbcTemplate jdbcTemplate;
	private final String getCurrentUserSql = "SELECT username, bio, image FROM users WHERE token = ?";
	private final String isFollowingSql = "SELECT following FROM follow WHERE follow_username = ?";
	
	public CommentDao(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public UserDto getCurrentUser(HttpSession httpSession) {
		return jdbcTemplate.queryForObject(getCurrentUserSql, new RowMapper<UserDto>() {
			@Override
			public UserDto mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
				UserDto userDto = new UserDto();

				userDto.setUsername(resultSet.getString("username"));
				userDto.setBio(resultSet.getString("bio"));
				userDto.setImage(resultSet.getString("image"));

				return userDto;
			}
		}, httpSession.getAttribute("Token"));
	}
	
	public boolean isFollowing(String username) {
		List<Integer> followingResult = jdbcTemplate.query(isFollowingSql, new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
				return resultSet.getInt("following");
			}
		}, username);

		for (int i = 0; i < followingResult.size(); i++) {
			if (followingResult.get(i) == 1) {
				return true;
			}
		}

		return false;
	}
	
	@Override
	public HashMap<String, CommentResponse> addComment(String slug, HashMap<String, CommentDto> comment, HttpSession httpSession) {
		final String insertSql = "INSERT INTO comments (id, slug, createdat, updatedat, body, username) VALUES (id_sequence.NEXTVAL, ?, ?, ?, ?, ?)";
		final String selectSequenceSql = "SELECT id_sequence.CURRVAL FROM DUAL";
		String now = LocalDateTime.now().toString();
		HashMap<String, CommentResponse> response = new HashMap<String, CommentResponse>();
		UserDto currentUser = getCurrentUser(httpSession);
		
		int insertResult = jdbcTemplate.update(insertSql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setString(1, slug);
				preparedStatement.setString(2, now);
				preparedStatement.setString(3, now);
				preparedStatement.setString(4, comment.get("comment").getBody());
				preparedStatement.setString(5, currentUser.getUsername());
			}
		});
		
		if (insertResult == 1) {
			CommentResponse commentResponse = new CommentResponse();
			AuthorDto authorDto = new AuthorDto();
			
			authorDto.setUsername(currentUser.getUsername());
			authorDto.setBio(currentUser.getBio());
			authorDto.setImage(currentUser.getImage());
			authorDto.setFollowing(isFollowing(currentUser.getUsername()));
			
			commentResponse.setId(jdbcTemplate.queryForObject(selectSequenceSql, Integer.class));
			commentResponse.setCreatedAt(now);
			commentResponse.setUpdatedAt(now);
			commentResponse.setBody(comment.get("comment").getBody());
			commentResponse.setAuthor(authorDto);
			
			response.put("comment", commentResponse);
		}

		return response;
	}

	@Override
	public HashMap<String, ArrayList<CommentResponse>> getComments(String slug, HttpSession httpSession) {
		final String selectUserSql = "SELECT bio, image FROM users WHERE username = ?";
		HashMap<String, ArrayList<CommentResponse>> response = new HashMap<String, ArrayList<CommentResponse>>();
		
		if (httpSession.getAttribute("Token") == null) {
			String selectCommentsSql = "SELECT id, createdat, updatedat, body, username FROM comments WHERE slug = ?";
			
			List<CommentResponse> selectCommentsResult= jdbcTemplate.query(selectCommentsSql, new RowMapper<CommentResponse>() {
				@Override
				public CommentResponse mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
					CommentResponse commentResponse = new CommentResponse();
					
					commentResponse.setId(resultSet.getInt("id"));
					commentResponse.setCreatedAt(resultSet.getString("createdat"));
					commentResponse.setUpdatedAt(resultSet.getString("updatedat"));
					commentResponse.setBody(resultSet.getString("body"));
					
					String username = resultSet.getString("username");
					
					AuthorDto author = jdbcTemplate.queryForObject(selectUserSql, new RowMapper<AuthorDto>() {
						@Override
						public AuthorDto mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
							AuthorDto authorDto = new AuthorDto();
							
							authorDto.setUsername(username);
							authorDto.setBio(resultSet.getString("bio"));
							authorDto.setImage(resultSet.getString("image"));
							authorDto.setFollowing(isFollowing(username));
							
							return authorDto;
						}
					}, username);
					
					commentResponse.setAuthor(author);
					
					return commentResponse;
				}
			}, slug);
			
			response.put("comments", (ArrayList<CommentResponse>) selectCommentsResult);
		}
		
		if (httpSession.getAttribute("Token") != null) {
			String selectCommentsSql = "SELECT id, createdat, updatedat, body, username FROM comments WHERE slug = ? AND username = ?";
			UserDto currentUser = getCurrentUser(httpSession);
			
			List<CommentResponse> selectCommentsResult= jdbcTemplate.query(selectCommentsSql, new RowMapper<CommentResponse>() {
				@Override
				public CommentResponse mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
					CommentResponse commentResponse = new CommentResponse();
					
					commentResponse.setId(resultSet.getInt("id"));
					commentResponse.setCreatedAt(resultSet.getString("createdat"));
					commentResponse.setUpdatedAt(resultSet.getString("updatedat"));
					commentResponse.setBody(resultSet.getString("body"));
					
					String username = resultSet.getString("username");
					
					AuthorDto author = jdbcTemplate.queryForObject(selectUserSql, new RowMapper<AuthorDto>() {
						@Override
						public AuthorDto mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
							AuthorDto authorDto = new AuthorDto();
							
							authorDto.setUsername(username);
							authorDto.setBio(resultSet.getString("bio"));
							authorDto.setImage(resultSet.getString("image"));
							authorDto.setFollowing(isFollowing(username));
							
							return authorDto;
						}
					}, username);
					
					commentResponse.setAuthor(author);
					
					return commentResponse;
				}
			}, slug, currentUser.getUsername());
			
			response.put("comments", (ArrayList<CommentResponse>) selectCommentsResult);
		}
		
		return response;
	}

	@Override
	public void deleteComment(String slug, int id, HttpSession httpSession) {
		final String selectSql = "SELECT username FROM comments WHERE slug = ? AND id = ?";
		final String deleteSql = "DELETE FROM comments WHERE slug = ? AND id = ?";
		UserDto currentUser = getCurrentUser(httpSession);
		
		String username = jdbcTemplate.queryForObject(selectSql, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
				return resultSet.getString("username");
			}
		}, slug, id);
		
		if (currentUser.getUsername().equals(username)) {
			jdbcTemplate.update(deleteSql, new PreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement preparedStatement) throws SQLException {
					preparedStatement.setString(1, slug);
					preparedStatement.setInt(2, id);
				}
			});
		}
	}
}
