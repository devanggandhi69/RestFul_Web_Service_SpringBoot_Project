package challenge.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import challenge.entity.User;

//import entity.User;

public interface UserDao {

	Collection<User> getAllUser();	
	List<User> getAllMessage(User user);
	List<User> getMessageWithKeyWord(User user, String search);
	List<Map<String, String>> getListFollowersAndFollowing(User user);
	Map<String,String> startFollowing(int personId, int followerId);
	Map<String,String> removeFollowing(int personId, int followerId);
	List<Integer> getAllPersonId();
	List<Integer> getFollowing(int current);
	boolean userAlreadyFollow(int personId, int followerId);
}