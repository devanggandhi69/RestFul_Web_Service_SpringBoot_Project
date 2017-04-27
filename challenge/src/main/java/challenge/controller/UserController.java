package challenge.controller;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import challenge.entity.User;
import challenge.service.UserService;

@RestController

@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	private String password = "password";

	// endpoint to read the message list for the current user.
	// Include messages they have sent and messages sent by users they follow
	@RequestMapping(method = RequestMethod.GET)
	public List<User> getAllMessage(@RequestHeader("Authorization") String authorization) {

		final String[] credentials = getCredentialsFromHeader(authorization);
		if (credentials.length == 0)
			return userService.userDoesNotExist();

		if (userService.peopleIdExist(Integer.valueOf(credentials[0])) && credentials[1].equals(password)) {
			User user = new User();
			user.setId(Integer.parseInt(credentials[0]));
			return userService.getAllMessage(user);
		}
		return userService.userDoesNotExist();
	}

	//Extract credentials from http header
	private String[] getCredentialsFromHeader(String authorization) {

		if (authorization != null && authorization.startsWith("Basic")) {
			// Authorization: Basic base64credentials
			String base64Credentials = authorization.substring("Basic".length()).trim();
			String credentials = new String(Base64.getDecoder().decode(base64Credentials), Charset.forName("UTF-8"));
			// credentials = username:password
			final String[] values = credentials.split(":", 2);
			return values;
		}
		return new String[0];
	}

	/*"search=” parameter that can be used to further filter messages based on keyword.*/
	@RequestMapping(params = { "search" }, method = RequestMethod.GET)
	public List<User> getMessageWithKeyWord(@RequestParam(value = "search") String keyWord,
			@RequestHeader("Authorization") String authorization) {
		final String[] credentials = getCredentialsFromHeader(authorization);

		if (credentials.length == 0)
			return userService.userDoesNotExist();

		if (userService.peopleIdExist(Integer.valueOf(credentials[0])) && credentials[1].equals(password)) {
			User user = new User();
			user.setId(Integer.parseInt(credentials[0]));
			return userService.getMessageWithKeyWord(user, keyWord);
		}
		return userService.userDoesNotExist();
	}

	/*Endpoints to get the list of people the user is following as well as the followers of the user.*/
	@RequestMapping(value = "/connections", method = RequestMethod.GET)
	public List<Map<String, String>> getListFollowersAndFollowing(
			@RequestHeader("Authorization") String authorization) {
		final String[] credentials = getCredentialsFromHeader(authorization);
		User user = new User();

		if (credentials.length != 0 && userService.peopleIdExist(Integer.valueOf(credentials[0]))
				&& credentials[1].equals(password)) {
			user.setId(Integer.valueOf(credentials[0]));
		}
		return userService.getListFollowersAndFollowing(user);
	}

	/*An endpoint to start following another user.*/
	@RequestMapping(value = "/follow", params = { "uid" }, method = RequestMethod.PUT)
	public Map<String, String> startFollowing(@RequestParam("uid") int followerId,
			@RequestHeader("Authorization") String authorization) {
		final String[] credentials = getCredentialsFromHeader(authorization);
		return userService.startFollowing(
				credentials.length == 0 ? 0 : credentials[1].equals(password) ? Integer.valueOf(credentials[0]) : 0,
				followerId);
	}

	/*An endpoint to unfollow another user.*/
	@RequestMapping(value = "/unfollow", params = { "uid" }, method = RequestMethod.DELETE)
	public Map<String, String> removeFollowing(@RequestHeader("Authorization") String authorization,
			@RequestParam("uid") int followerId) {
		final String[] credentials = getCredentialsFromHeader(authorization);
		return userService.removeFollowing(
				credentials.length == 0 ? 0 : credentials[1].equals(password) ? Integer.valueOf(credentials[0]) : 0,
				followerId);
	}

	/*
	 * An endpoint that returns the current user's "shortest distance" to some other user. 
	The shortest distance is defined as the number of hops needed to reach a
 	user through the users you are following
 	*/
	@RequestMapping(value = "/distance", params = { "uid" }, method = RequestMethod.GET)
	public List<Integer> getSortestDistance(@RequestHeader("Authorization") String authorization,
			@RequestParam("uid") int anotherUserId) {
		final String[] credentials = getCredentialsFromHeader(authorization);
		return userService.getSortestDistance(
				credentials.length == 0 ? 0 : credentials[1].equals(password) ? Integer.valueOf(credentials[0]) : 0,
				anotherUserId);
	}
}
