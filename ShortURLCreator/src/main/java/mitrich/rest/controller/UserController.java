package mitrich.rest.controller;

import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletResponse;

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
			user.setPassword(userService.passwordEncode(user.getPassword() + user.getUserName()));
			userService.save(user);
			return "{\"message\":\"OK\"}";
		} else
			return "{\"message\":\"This user name is already taken\"}";
	}

	@RequestMapping(value = "/login/", method = RequestMethod.POST)
	public String loginUser(@RequestBody User usr, HttpServletResponse httpServletResponse)
			throws NoSuchAlgorithmException {

		User user = userService.findByUserName(usr.getUserName());

		if (user == null) {
			return "{\"message\":\"Invalid user name\"}";
		}
		if (!userService.passwordEncode(usr.getPassword() + usr.getUserName()).equals(user.getPassword())) {
			return "{\"message\":\"Invalid password\"}";
		}

		httpServletResponse.addHeader("JWE-Token", "123456789abcdef");
		return "{\"message\":\"OK\"}";
	}

}
