package mitrich.rest.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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
		if (this.userService.findByUserName(user.getUserName()) == null) {
			user.setPassword(passwordEncode(user.getPassword()));
			this.userService.save(user);
			return "{\"message\":\"OK\"}";
		} else
			return "{\"message\":\"This user name is already taken\"}";
	}

	@RequestMapping(value = "/user/{userName}", method = RequestMethod.POST)
	public String findUser(@PathVariable String userName, @RequestBody User usr) throws NoSuchAlgorithmException {

		User user = this.userService.findByUserName(userName);
		String password = usr.getPassword();

		if (user == null) {
			return "{\"message\":\"Invalid user name\"}";
		}
		if (!passwordEncode(password).equals(user.getPassword())) {
			return "{\"message\":\"Invalid password\"}";
		}

		return "{\"message\":\"OK\"}";
	}

	private String passwordEncode(String password) throws NoSuchAlgorithmException {
		MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
		byte[] result = mDigest.digest(password.getBytes());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < result.length; i++) {
			String hex = Integer.toHexString(0xff & result[i]);
			if (hex.length() == 1)
				sb.append('0');
			sb.append(hex);
		}
		return sb.toString();
	}

}
