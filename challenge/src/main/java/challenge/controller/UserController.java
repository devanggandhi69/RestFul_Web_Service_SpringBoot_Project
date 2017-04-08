package challenge.controller;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	@RequestMapping(method = RequestMethod.GET)
	public Collection<User> getAllUser(){
		return userService.getAllUser();
	}
	
	//endpoint to read the message list for the current user.
	//Include messages they have sent and messages sent by users they follow
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public List<User> getAllMessage(@PathVariable("id") int id) {		
		User user = new User();
		user.setId(id);
		return userService.getAllMessage(user);
	}
	
	//"search=” parameter that can be used to further filter messages based on keyword.
	//"id" is current user id
	@RequestMapping(value = "/{id}", params = {"search"}, method = RequestMethod.GET)
	public List<User> getMessageWithKeyWord(@PathVariable("id") int id, @RequestParam(value = "search") String keyWord) {		
		User user = new User();
		user.setId(id);
		return userService.getMessageWithKeyWord(user, keyWord);
	} 
	
	//Endpoints to get the list of people the user is following as well as the followers of the user.
	@RequestMapping(value = "/{id}/connections", method = RequestMethod.GET)
	public List<Map<String,String>> getListFollowersAndFollowing(@PathVariable("id") int id){
		User user = new User();
		user.setId(id);
		return userService.getListFollowersAndFollowing(user);
	}
	
	//An endpoint to start following another user.
	@RequestMapping(value = "/{persionId}/follow/{followerId}", method = RequestMethod.PUT)
	public Map<String, String> startFollowing(@PathVariable("persionId") int personId, @PathVariable("followerId") int followerId){		
		return userService.startFollowing(personId, followerId);
	}
	
	//An endpoint to unfollow another user.
	@RequestMapping(value = "/{persionId}/unfollow/{followerId}", method = RequestMethod.DELETE)
	public Map<String, String> removeFollowing(@PathVariable("persionId") int personId, @PathVariable("followerId") int followerId) {
		return userService.removeFollowing(personId, followerId);
	}
	
	//An endpoint that returns the current user's "shortest distance" to some other user. 
	//The shortest distance is defined as the number of hops needed to reach a user through the users you are following
	@RequestMapping(value = "{persionId}/distance/{anotherUserId}", method = RequestMethod.GET)
	public List<Integer> getSortestDistance(@PathVariable("persionId") int personId, @PathVariable("anotherUserId") int anotherUserId){
		return userService.getSortestDistance(personId, anotherUserId);
	}
}
