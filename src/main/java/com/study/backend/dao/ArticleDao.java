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

	public int checkFavorite(String username, String slug) {
		final String selectFavoriteCheckSql = "SELECT count(*) count FROM favorite WHERE username = ? AND slug = ?";

		return jdbcTemplate.queryForObject(selectFavoriteCheckSql, new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
				return resultSet.getInt("count");
			}
		}, username, slug);
	}

	@Override
	public HashMap<String, ArticleResponse> createArticle(HashMap<String, ArticleDto> article,
			HttpSession httpSession) {
		final String insertTagSql = "INSERT INTO tags (slug, tag) VALUES (?, ?)";
		final String insertTagListSql = "INSERT INTO taglist (tag) VALUES (?)";
		final String insertArticleSql = "INSERT INTO article (slug, username, title, description, body, createdAt, updatedAt) VALUES (?, ?, ?, ?, ?, ?, ?)";
		String now = LocalDateTime.now().toString();
		HashMap<String, ArticleResponse> response = new HashMap<String, ArticleResponse>();
		UserDto currentUser = getCurrentUser(httpSession);

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
			List<String> tags = getTags();
			ArrayList<String> tagList = article.get("article").getTagList();

			for (int i = 0; i < tagList.size(); i++) {
				String tag = tagList.get(i);

				if (tags.contains(tag)) {
					jdbcTemplate.update(insertTagSql, new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement preparedStatement) throws SQLException {
							preparedStatement.setString(1, article.get("article").getTitle().replace(" ", "-"));
							preparedStatement.setString(2, tag);
						}
					});
				} else {
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
			authorDto.setFollowing(isFollowing(currentUser.getUsername()));

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

	public HashMap<String, ArticleResponse> getArticle(String slug, HttpSession httpSession) {
		final String selectArticleSql = "SELECT slug, username, title, description, body, createdat, updatedat, favoritescount FROM article WHERE slug = ?";
		final String selectUserTagsSql = "SELECT tag FROM tags WHERE slug = ?";
		final String selectUserSql = "SELECT username, bio, image FROM users WHERE username = ?";

		HashMap<String, ArticleResponse> response = new HashMap<>();
		ArticleResponse articleResponse = new ArticleResponse();
		AuthorDto authorDto = new AuthorDto();

		ArticleResponse selectArticleResult = jdbcTemplate.queryForObject(selectArticleSql,
				new RowMapper<ArticleResponse>() {
					@Override
					public ArticleResponse mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
						articleResponse.setSlug(resultSet.getString("slug"));
						articleResponse.setTitle(resultSet.getString("title"));
						articleResponse.setDescription(resultSet.getString("description"));
						articleResponse.setBody(resultSet.getString("body"));
						articleResponse.setCreatedAt(resultSet.getString("createdat"));
						articleResponse.setUpdatedAt(resultSet.getString("updatedat"));

						if (httpSession.getAttribute("Token") == null) {
							articleResponse.setFavorited(false);
						} else {
							if (checkFavorite(getCurrentUser(httpSession).getUsername(), slug) == 0) {
								articleResponse.setFavorited(false);
							} else {
								articleResponse.setFavorited(true);
							}
						}

						articleResponse.setFavoritesCount(resultSet.getInt("favoritescount"));

						authorDto.setUsername(resultSet.getString("username"));

						return articleResponse;
					}
				}, slug);

		List<String> selectUserTagsResult = jdbcTemplate.query(selectUserTagsSql, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
				return resultSet.getString("tag");
			}
		}, selectArticleResult.getSlug());

		selectArticleResult.setTagList((ArrayList<String>) selectUserTagsResult);

		AuthorDto selectUserResult = jdbcTemplate.queryForObject(selectUserSql, new RowMapper<AuthorDto>() {
			@Override
			public AuthorDto mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
				authorDto.setBio(resultSet.getString("bio"));
				authorDto.setImage(resultSet.getString("image"));
				authorDto.setFollowing(isFollowing(resultSet.getString("username")));

				return authorDto;
			}
		}, authorDto.getUsername());

		selectArticleResult.setAuthor(selectUserResult);

		response.put("article", selectArticleResult);

		return response;
	}

	@Override
	public HashMap<String, ArticleResponse> updateArticle(String slug, HashMap<String, ArticleDto> article,
			HttpSession httpSession) {
		final String selectSql = "SELECT username, title, description, body FROM article WHERE slug = ?";
		final String updateSql = "UPDATE article SET slug = ?, title = ?, description = ?, body = ?, updatedat = ? WHERE slug = ?";
		String now = LocalDateTime.now().toString();
		ArticleDto updateArticle = new ArticleDto();
		UserDto currentUser = getCurrentUser(httpSession);

		ArticleDto selectResult = jdbcTemplate.queryForObject(selectSql, new RowMapper<ArticleDto>() {
			@Override
			public ArticleDto mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
				ArticleDto articleDto = new ArticleDto();

				articleDto.setUsername(resultSet.getString("username"));
				articleDto.setTitle(resultSet.getString("title"));
				articleDto.setDescription(resultSet.getString("description"));
				articleDto.setBody(resultSet.getString("body"));

				return articleDto;
			}
		}, slug);

		if (currentUser.getUsername().equals(selectResult.getUsername())) {
			updateArticle.setTitle(selectResult.getTitle());
			updateArticle.setDescription(selectResult.getDescription());
			updateArticle.setBody(selectResult.getBody());

			if (article.get("article").getTitle() != null) {
				updateArticle.setTitle(article.get("article").getTitle());
			}
			if (article.get("article").getDescription() != null) {
				updateArticle.setDescription(article.get("article").getDescription());
			}
			if (article.get("article").getBody() != null) {
				updateArticle.setBody(article.get("article").getBody());
			}

			int updateResult = jdbcTemplate.update(updateSql, new PreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement preparedStatement) throws SQLException {
					preparedStatement.setString(1, updateArticle.getTitle().replace(" ", "-"));
					preparedStatement.setString(2, updateArticle.getTitle());
					preparedStatement.setString(3, updateArticle.getDescription());
					preparedStatement.setString(4, updateArticle.getBody());
					preparedStatement.setString(5, now);
					preparedStatement.setString(6, slug);
				}
			});

			if (updateResult == 1) {
				return getArticle(updateArticle.getTitle().replace(" ", "-"), httpSession);
			}
		}

		return null;
	}

	@Override
	public void deleteArticle(String slug, HttpSession httpSession) {
		final String selectArticleSql = "SELECT username FROM article WHERE slug = ?";
		final String selectTagsSql = "SELECT DISTINCT tag FROM tags";
		final String deleteTagsSql = "DELETE FROM tags WHERE slug = ?";
		final String deleteArticleSql = "DELETE FROM article WHERE slug = ?";
		final String deleteTagListSql = "DELETE FROM taglist WHERE tag = ?";
		UserDto currentUser = getCurrentUser(httpSession);

		String username = jdbcTemplate.queryForObject(selectArticleSql, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
				return resultSet.getString("username");
			}
		}, slug);

		if (currentUser.getUsername().equals(username)) {
			int deleteTagsResult = jdbcTemplate.update(deleteTagsSql, new PreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement preparedStatement) throws SQLException {
					preparedStatement.setString(1, slug);
				}
			});

			jdbcTemplate.update(deleteArticleSql, new PreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement preparedStatement) throws SQLException {
					preparedStatement.setString(1, slug);
				}
			});

			if (deleteTagsResult > 0) {
				List<String> tags = getTags();
				List<String> tagList = jdbcTemplate.query(selectTagsSql, new RowMapper<String>() {
					@Override
					public String mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
						return resultSet.getString("tag");
					}
				});

				for (int i = 0; i < tags.size(); i++) {
					String tag = tags.get(i);

					if (!tagList.contains(tag)) {
						jdbcTemplate.update(deleteTagListSql, new PreparedStatementSetter() {
							@Override
							public void setValues(PreparedStatement preparedStatement) throws SQLException {
								preparedStatement.setString(1, tag);
							}
						});
					}
				}
			}
		}
	}

	@Override
	public HashMap<String, ArticleResponse> favoriteArticle(String slug, HttpSession httpSession) {
		final String updateFavoriteSql = "UPDATE article SET favoritescount = favoritescount + 1 WHERE slug = ?";
		final String insertFavoriteCheckSql = "INSERT INTO favorite (username, slug) VALUES (?, ?)";
		String currentUsername = getCurrentUser(httpSession).getUsername();

		if (checkFavorite(currentUsername, slug) == 0) {
			int updateFavoriteResult = jdbcTemplate.update(updateFavoriteSql, new PreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement preparedStatement) throws SQLException {
					preparedStatement.setString(1, slug);
				}
			});

			int insertFavoriteCheckResult = jdbcTemplate.update(insertFavoriteCheckSql, new PreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement preparedStatement) throws SQLException {
					preparedStatement.setString(1, currentUsername);
					preparedStatement.setString(2, slug);
				}
			});

			if (updateFavoriteResult == 1 && insertFavoriteCheckResult == 1) {
				return getArticle(slug, httpSession);
			}
		}

		return getArticle(slug, httpSession);
	}

	@Override
	public HashMap<String, ArticleResponse> unfavoriteArticle(String slug, HttpSession httpSession) {
		final String updateUnfavoriteSql = "UPDATE article SET favoritescount = favoritescount - 1 WHERE slug = ?";
		final String deleteFavoriteCheckSql = "DELETE FROM favorite WHERE username = ? AND slug = ?";
		String currentUsername = getCurrentUser(httpSession).getUsername();
		
		if (checkFavorite(currentUsername, slug) == 1) {
			int updateUnfavoriteResult = jdbcTemplate.update(updateUnfavoriteSql, new PreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement preparedStatement) throws SQLException {
					preparedStatement.setString(1, slug);
				}
			});
			
			int deleteFavoriteCheckResult = jdbcTemplate.update(deleteFavoriteCheckSql, new PreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement preparedStatement) throws SQLException {
					preparedStatement.setString(1, currentUsername);
					preparedStatement.setString(2, slug);
				}
			});
			
			if (updateUnfavoriteResult == 1 && deleteFavoriteCheckResult == 1) {
				return getArticle(slug, httpSession);
			}
			
		}
		
		return getArticle(slug, httpSession);
	}
}
