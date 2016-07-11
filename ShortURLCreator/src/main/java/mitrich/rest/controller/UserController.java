package mitrich.rest.controller;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

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
import mitrich.rest.security.JwtTokenUtil;
import mitrich.rest.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@RequestMapping(value = "/users/", method = RequestMethod.POST)
	public ResponseEntity<?> createUser(@RequestBody @Valid User usr, BindingResult result)
			throws NoSuchAlgorithmException {

		if (result.hasErrors()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		if (userService.findByUserName(usr.getUserName()) != null) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

		usr.setPassword(userService.passwordEncode(usr.getPassword() + usr.getUserName()));
		usr.setRole("ROLE_USER");
		User user = userService.save(usr);

		Map<String, String> token = new HashMap<String, String>();
		token.put("access_token", jwtTokenUtil.generateToken(user));
		return new ResponseEntity<>(token, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/login/", method = RequestMethod.POST)
	public ResponseEntity<?> loginUser(@RequestBody @Valid User usr, BindingResult result)
			throws NoSuchAlgorithmException {

		if (result.hasErrors()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		User user = userService.findByUserName(usr.getUserName());

		if (user == null
				|| !userService.passwordEncode(usr.getPassword() + usr.getUserName()).equals(user.getPassword())) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		Map<String, String> token = new HashMap<String, String>();
		token.put("access_token", jwtTokenUtil.generateToken(user));
		return new ResponseEntity<>(token, HttpStatus.OK);
	}

}
