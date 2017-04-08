package challenge.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Currency;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import challenge.dao.UserDao;
import challenge.entity.User;

//import dao.UserDao;
//import entity.User;

@Service
public class UserService {
	@Autowired
	private UserDao userDao;

	//get all users
	public Collection<User> getAllUser() {
		return userDao.getAllUser();
	}

	//get all messages of user
	public List<User> getAllMessage(User user) {
		if (!peopleIdExist(user.getId())) {
			return userDoesNotExist();
		}
		return userDao.getAllMessage(user);
	}

	//get messages with specific keyword
	public List<User> getMessageWithKeyWord(User user, String search) {
		if (!peopleIdExist(user.getId())) {
			return userDoesNotExist();
		}
		return userDao.getMessageWithKeyWord(user, search);
	}

	//check user exist or not
	private List<User> userDoesNotExist() {
		User userNotExist = new User();
		userNotExist.setName(null);
		userNotExist.setContent("User does not exist");
		List<User> list = new ArrayList<User>();
		list.add(userNotExist);
		return list;
	}

	//get list of follower and following
	public List<Map<String, String>> getListFollowersAndFollowing(User user) {
		if (!peopleIdExist(user.getId())) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("Message", "User Does not exist");
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			list.add(map);
			return list;
		}
		return userDao.getListFollowersAndFollowing(user);
	}

	//start following
	public Map<String, String> startFollowing(int personId, int followerId) {
		// Check that user is exist or not
		Map<String, String> map = new HashMap<>();
		String key = "Info";
		boolean possible = followUnfollowPossible(personId, followerId, map, key);
		if (!possible) {
			return map;
		}
		boolean follow = userDao.userAlreadyFollow(personId, followerId);
		String alreadyExist = "Already Exist";
		if (follow) {
			map.put(key, alreadyExist);
			return map;
		}

		return userDao.startFollowing(personId, followerId);
	}

	private boolean followUnfollowPossible(int personId, int followerId, Map<String, String> map, String key) {
		if (personId != followerId) {
			if (!peopleIdExist(personId, followerId)) {
				String doesNotExist = "User does not exist";
				map.put(key, doesNotExist);
				return false;
			}
		} else if (personId == followerId) {
			String notPossible = "User can not follow itself";
			map.put(key, notPossible);
			return false;
		}
		return true;
	}

	//remove connections
	public Map<String, String> removeFollowing(int personId, int followerId) {
		// Check that user is exist or not
		Map<String, String> map = new HashMap<>();
		String key = "Info";
		boolean possible = followUnfollowPossible(personId, followerId, map, key);
		if (!possible) {
			return map;
		}

		// Check that user is already following or not
		boolean follow = userDao.userAlreadyFollow(personId, followerId);
		String alreadyExist = "No connection found with user";
		if (!follow) {
			map.put(key, alreadyExist);
			return map;
		}

		return userDao.removeFollowing(personId, followerId);
	}

	// Check that people is exist or not
	private boolean peopleIdExist(int personId, int followerId) {
		List<Integer> personIdList = userDao.getAllPersonId();
		if (personIdList.contains(personId) && personIdList.contains(followerId))
			return true;
		return false;
	}
	
	// Check that people is exist or not
	private boolean peopleIdExist(int personId) {
		List<Integer> personIdList = userDao.getAllPersonId();
		if (personIdList.contains(personId))
			return true;
		return false;
	}

	//get distance between two user
	public List<Integer> getSortestDistance(int personId, int anotherPersonId) {
		int distance = 0;
		List<Integer> list = new ArrayList<>();
		if (!peopleIdExist(personId, anotherPersonId)) {
			list.add(-1);
			return list;
		}
		if (personId == anotherPersonId) {
			list.add(distance);
			return list;
		}

		Queue<Integer> queue = new LinkedList<Integer>();
		Set<Integer> set = new HashSet<Integer>();
		queue.add(personId);
		set.add(personId);
		Map<Integer, Integer> childParent = new HashMap<>();
		childParent.put(personId, -1);
		boolean match = false;
		while (queue.size() > 0) {

			int current = queue.poll();
			List<Integer> neighbour = userDao.getFollowing(current);
			for (int id : neighbour) {
				if (id == anotherPersonId) {
					// list.add(distance);
					childParent.put(id, current);
					match = true;
					break;
				}
				if (set.add(id)) {
					childParent.put(id, current);
					queue.add(id);
				}
			}
			if (match)
				break;
		}
		if (match == false) {
			list.add(-1);
			return list;
		} else {
			int parentKey = anotherPersonId;
			for (int i = 0; i < childParent.size(); i++) {
				parentKey = childParent.get(parentKey);
				if (parentKey == -1) {					
					break;
				}
				distance++;
			}
		}
		list.add(distance);
		return list;
	}
}
