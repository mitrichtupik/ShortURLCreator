package mitrich.rest.controller;

import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import mitrich.rest.model.User;
import mitrich.rest.model.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/user/", method = RequestMethod.POST)
	public String createUser(@RequestBody User user) throws NoSuchAlgorithmException {
		if (userService.findByUserName(user.getUserName()) == null) {
			user.setPassword(userService.passwordEncode(user.getPassword()));
			userService.save(user);
			return "{\"message\":\"OK\"}";
		} else
			return "{\"message\":\"This user name is already taken\"}";
	}

	@RequestMapping(value = "/login/", method = RequestMethod.POST)
	public String loginUser(@RequestBody User usr) throws NoSuchAlgorithmException {

		User user = userService.findByUserName(usr.getUserName());

		if (user == null) {
			return "{\"message\":\"Invalid user name\"}";
		}
		if (!userService.passwordEncode(usr.getPassword()).equals(user.getPassword())) {
			return "{\"message\":\"Invalid password\"}";
		}

		return "{\"message\":\"OK\"}";
	}

}
