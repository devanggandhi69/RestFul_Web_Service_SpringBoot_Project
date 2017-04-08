package challenge.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import challenge.entity.User;

@Repository
public class UserDaoImp implements UserDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see dao.UserDao#getAllUser()
	 */
	private NamedParameterJdbcTemplate jdbc;

	public UserDaoImp(final NamedParameterJdbcTemplate jdbc) {
		// TODO Auto-generated constructor stub
		this.jdbc = jdbc;
	}

	//get all user
	@Override
	public Collection<User> getAllUser() {
		final String SELECT_ALL_SQL = "SELECT * FROM PERSON";
		List<User> users = jdbc.query(SELECT_ALL_SQL, new RowMapper<User>() {

			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				// TODO Auto-generated method stub
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setName(rs.getString("name"));
				return user;
			}
		});
		return users;
	}

	//get all messages related to user
	@Override
	public List<User> getAllMessage(User user) throws DataAccessException {
		// TODO Auto-generated method stub

		List<User> users = getMessagesForUser(user, false, new String());

		return users;
	}

	//helper method to get all messages
	private List<User> getMessagesForUser(User user, boolean find, String keyWord) throws DataAccessException {
		int id = user.getId();
		final String SELECT_ALL_CONTENT_WITH_NAME;

		MapSqlParameterSource namedParams = new MapSqlParameterSource();
		if (!find) {
			SELECT_ALL_CONTENT_WITH_NAME = "select person.id, person.name, follow.content from person inner join "
					+ "(SELECT tweet.person_id, tweet.content FROM tweet where tweet.person_id in "
					+ "(select distinct followers.person_id from followers where follower_person_id = :id) or tweet.person_id = :id) follow on "
					+ "person.id = follow.person_id order by person.id";
		} else {
			SELECT_ALL_CONTENT_WITH_NAME = "select person.id, person.name, follow.content from person inner join "
					+ "(SELECT tweet.person_id, tweet.content FROM tweet where tweet.person_id in "
					+ "(select distinct followers.person_id from followers where follower_person_id = :id) "
					+ "or tweet.person_id = :id) follow on person.id = follow.person_id where LOWER(follow.content) LIKE :keyWord order by person.id";
			keyWord = "% " + keyWord.toLowerCase().trim() + " %";
			namedParams.addValue("keyWord", keyWord);
		}
		namedParams.addValue("id", id);
		List<User> users = jdbc.query(SELECT_ALL_CONTENT_WITH_NAME, namedParams, new RowMapper<User>() {

			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				// TODO Auto-generated method stub
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setName(rs.getString("name"));
				user.setContent(rs.getString("content"));
				return user;
			}
		});
		return users;
	}

	//get all messages with keyword
	@Override
	public List<User> getMessageWithKeyWord(User user, String search) {
		// TODO Auto-generated method stub
		List<User> users = getMessagesForUser(user, true, search);

		return users;

	}

	//get all followers and following of the user
	@Override
	public List<Map<String, String>> getListFollowersAndFollowing(User user) throws DataAccessException {
		// TODO Auto-generated method stub
		int id = user.getId();
		final String SELECT_ALL_FOLLOWER_AND_FOLLOWING = "SELECT * FROM person WHERE id IN "
				+ "(SELECT follower_person_id FROM followers WHERE person_id = :id GROUP BY follower_person_id) OR "
				+ "id IN (SELECT person_id FROM followers WHERE follower_person_id = :id GROUP BY person_id)";
		MapSqlParameterSource namedParams = new MapSqlParameterSource();
		namedParams.addValue("id", id);
		List<Map<String, String>> users = jdbc.query(SELECT_ALL_FOLLOWER_AND_FOLLOWING, namedParams,
				new RowMapper<Map<String, String>>() {

					@Override
					public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
						// TODO Auto-generated method stub
						Map<String, String> user = new HashMap<String, String>();
						user.put("id", rs.getString("id"));
						user.put("name", rs.getString("name"));
						return user;
					}
				});
		return users;
	}

	//start following
	@Override
	public Map<String,String> startFollowing(int personId, int followerId) throws DataAccessException {
		// TODO Auto-generated method stub
		 
		MapSqlParameterSource namedParams = new MapSqlParameterSource();
		namedParams.addValue("id", getLastId());
		namedParams.addValue("personId", String.valueOf(personId));
		namedParams.addValue("followerId", String.valueOf(followerId));
		final String START_FOLLOWING = "INSERT INTO followers(id, person_id, follower_person_id) VALUES (:id, :followerId, :personId)";

		int output = jdbc.update(START_FOLLOWING, namedParams);
		Map<String, String> map = getResultMap(output);
		return map;
	}

	//helper method
	private Map<String, String> getResultMap(int output) {
		Map<String,String> map = new HashMap<>();
		String done = "Done";
		String tryAgain = "Please try again after some time";
		String info = "Info";
		String somethingWrong = "Something went wrong, Contact customer service";
		if (output == 1) {
			map.put(info, done);
			return map;
		} else if (output == 0) {
			map.put(info, tryAgain);
			return map;
		}
		map.put(info, somethingWrong);
		return map;
	}

	//helper method for get last id.
	private String getLastId() throws DataAccessException {
		final String lastIndex = "SELECT max(id) as max from followers";
		List<Integer> max = jdbc.query(lastIndex, new RowMapper<Integer>() {

			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				// TODO Auto-generated method stub
				return rs.getInt("max");
			}
		});
		return String.valueOf(max.get(0) + 1);
	}
	
	//get all person ID
	public List<Integer> getAllPersonId() throws DataAccessException {
		final String SELECT_ALL_USER = "SELECT id from person";

		List<Integer> personIdList = jdbc.query(SELECT_ALL_USER, new RowMapper<Integer>() {

			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				// TODO Auto-generated method stub
				Integer id = rs.getInt("id");

				return id;
			}
		});
		return personIdList;
	}

	//remove connections
	@Override
	public Map<String, String> removeFollowing(int personId, int followerId) throws DataAccessException {
		// TODO Auto-generated method stub				
		MapSqlParameterSource namedParams = new MapSqlParameterSource();
		namedParams.addValue("personId", String.valueOf(personId));
		namedParams.addValue("followerId", String.valueOf(followerId));
		final String UNFOLLOW_USER = "DELETE FROM followers where follower_person_id = :personId AND person_id = :followerId";
		int output = jdbc.update(UNFOLLOW_USER, namedParams);		
		Map<String, String> map = getResultMap(output);
		return map;
	}

	//get following of the user
	@Override
	public List<Integer> getFollowing(int current) {
		// TODO Auto-generated method stub
		final String SELECT_ALL_SQL = "select person_id from followers where follower_person_id = :id";
		MapSqlParameterSource namedParams = new MapSqlParameterSource();
		namedParams.addValue("id", String.valueOf(current));
		List<Integer> users = jdbc.query(SELECT_ALL_SQL, namedParams, new RowMapper<Integer>() {

			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				// TODO Auto-generated method stub
				
				return rs.getInt("person_id");
			}
		});
		return users;

	}

	//check that connections already exist
	@Override
	public boolean userAlreadyFollow(int personId, int followerId) {
		// TODO Auto-generated method stub
		MapSqlParameterSource namedParams = new MapSqlParameterSource();
		namedParams.addValue("personId", personId);
		namedParams.addValue("followerId", followerId);
		final String SELECT_FOLLOW = "select * from followers where follower_person_id = :personId and person_id = :followerId";
		List<String> follow = jdbc.query(SELECT_FOLLOW, namedParams, new RowMapper<String>() {

			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				// TODO Auto-generated method stub
				String followId = rs.getString("person_id");
				followId += " " + rs.getString("follower_person_id");
				return followId;
			}
		});
		if(follow.size()!=0)
			return true;
		
		return false;
					
	}

}
