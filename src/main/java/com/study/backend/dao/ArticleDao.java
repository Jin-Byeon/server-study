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

import com.study.backend.dto.ArticleDto;
import com.study.backend.dto.ArticleResponse;
import com.study.backend.dto.AuthorDto;
import com.study.backend.dto.UserDto;

@Repository
public class ArticleDao implements IArticleDao {
	private final JdbcTemplate jdbcTemplate;
	private final String getCurrentUserSql = "SELECT username, bio, image FROM users WHERE token = ?";
	private final String getTagsSql = "SELECT tag FROM taglist";
	private final String isFollowingSql = "SELECT following FROM follow WHERE follow_username = ?";

	public ArticleDao(JdbcTemplate jdbcTemplate) {
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

	public List<String> getTags() {
		return jdbcTemplate.query(getTagsSql, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
				return resultSet.getString("tag");
			}
		});
	}

	public boolean isFollowing(HttpSession httpSession) {
		List<Integer> followingResult = jdbcTemplate.query(isFollowingSql, new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
				return resultSet.getInt("following");
			}
		}, getCurrentUser(httpSession).getUsername());

		for (int i = 0; i < followingResult.size(); i++) {
			if (followingResult.get(i) == 1) {
				return true;
			}
		}

		return false;
	}

	@Override
	public HashMap<String, ArticleResponse> createArticle(HashMap<String, ArticleDto> article,
			HttpSession httpSession) {
		final String insertTagListSql = "INSERT INTO taglist (tag) VALUES (?)";
		final String insertTagSql = "INSERT INTO tags (slug, tag) VALUES (?, ?)";
		final String insertArticleSql = "INSERT INTO article (slug, username, title, description, body, createdAt, updatedAt) VALUES (?, ?, ?, ?, ?, ?, ?)";
		String now = LocalDateTime.now().toString();
		HashMap<String, ArticleResponse> response = new HashMap<String, ArticleResponse>();
		UserDto currentUser = getCurrentUser(httpSession);
		List<String> tags = getTags();

		int insertArticleResult = jdbcTemplate.update(insertArticleSql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setString(1, article.get("article").getTitle().replace(" ", "-"));
				preparedStatement.setString(2, currentUser.getUsername());
				preparedStatement.setString(3, article.get("article").getTitle());
				preparedStatement.setString(4, article.get("article").getDescription());
				preparedStatement.setString(5, article.get("article").getBody());
				preparedStatement.setString(6, now);
				preparedStatement.setString(7, now);
			}
		});

		if (article.get("article").getTagList() != null) {
			ArrayList<String> tagList = article.get("article").getTagList();
			
			for (int i = 0; i < tagList.size(); i++) {
				String tag = tagList.get(i);

				if (!tags.contains(tag)) {
					int insertTagListResult = jdbcTemplate.update(insertTagListSql, new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement preparedStatement) throws SQLException {
							preparedStatement.setString(1, tag);
						}
					});

					if (insertTagListResult == 1) {
						jdbcTemplate.update(insertTagSql, new PreparedStatementSetter() {
							@Override
							public void setValues(PreparedStatement preparedStatement) throws SQLException {
								preparedStatement.setString(1, article.get("article").getTitle().replace(" ", "-"));
								preparedStatement.setString(2, tag);
							}
						});
					}
				}
			}
		}

		if (insertArticleResult == 1) {
			ArticleResponse articleResponse = new ArticleResponse();
			AuthorDto authorDto = new AuthorDto();

			authorDto.setUsername(currentUser.getUsername());
			authorDto.setBio(currentUser.getBio());
			authorDto.setImage(currentUser.getImage());
			authorDto.setFollowing(isFollowing(httpSession));

			articleResponse.setSlug(article.get("article").getTitle().replace(" ", "-"));
			articleResponse.setTitle(article.get("article").getTitle());
			articleResponse.setDescription(article.get("article").getDescription());
			articleResponse.setBody(article.get("article").getBody());
			articleResponse.setTagList(article.get("article").getTagList());
			articleResponse.setCreatedAt(now);
			articleResponse.setUpdatedAt(now);
			articleResponse.setFavorited(false);
			articleResponse.setFavoritesCount(0);
			articleResponse.setAuthor(authorDto);

			response.put("article", articleResponse);
		}

		return response;
	}
}
