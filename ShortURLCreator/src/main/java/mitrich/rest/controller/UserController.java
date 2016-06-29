package mitrich.rest.controller;

import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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

	@RequestMapping(value = "/users/", method = RequestMethod.POST)
	public ResponseEntity<User> createUser(@RequestBody @Valid User user, BindingResult result)
			throws NoSuchAlgorithmException {

		if (result.hasErrors()) {
			return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
		}

		if (userService.findByUserName(user.getUserName()) != null) {
			return new ResponseEntity<User>(HttpStatus.CONFLICT);
		}

		user.setPassword(userService.passwordEncode(user.getPassword() + user.getUserName()));
		User saved_user = userService.save(user);
		return new ResponseEntity<User>(saved_user, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/login/", method = RequestMethod.POST)
	public ResponseEntity<User> loginUser(@RequestBody @Valid User usr, BindingResult result,
			HttpServletResponse httpServletResponse) throws NoSuchAlgorithmException {

		if (result.hasErrors()) {
			return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
		}

		User user = userService.findByUserName(usr.getUserName());

		if (user == null
				|| !userService.passwordEncode(usr.getPassword() + usr.getUserName()).equals(user.getPassword())) {
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}

		httpServletResponse.addHeader("JWE-Token", "123456789abcdef");
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

}
