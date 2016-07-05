package mitrich.rest.controller;

import java.security.NoSuchAlgorithmException;

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
	public ResponseEntity<String> createUser(@RequestBody @Valid User usr, BindingResult result)
			throws NoSuchAlgorithmException {

		if (result.hasErrors()) {
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}

		if (userService.findByUserName(usr.getUserName()) != null) {
			return new ResponseEntity<String>(HttpStatus.CONFLICT);
		}

		usr.setPassword(userService.passwordEncode(usr.getPassword() + usr.getUserName()));
		usr.setRole("ROLE_USER");
		User user = userService.save(usr);

		String token = "{\"access_token\":\"" + jwtTokenUtil.generateToken(user) + "\"}";
		return new ResponseEntity<String>(token, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/login/", method = RequestMethod.POST)
	public ResponseEntity<String> loginUser(@RequestBody @Valid User usr, BindingResult result)
			throws NoSuchAlgorithmException {

		if (result.hasErrors()) {
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}

		User user = userService.findByUserName(usr.getUserName());

		if (user == null
				|| !userService.passwordEncode(usr.getPassword() + usr.getUserName()).equals(user.getPassword())) {
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		}

		String token = "{\"access_token\":\"" + jwtTokenUtil.generateToken(user) + "\"}";
		return new ResponseEntity<String>(token, HttpStatus.OK);
	}

}
