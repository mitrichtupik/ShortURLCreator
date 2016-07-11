package mitrich.rest.controller;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.security.NoSuchAlgorithmException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import mitrich.rest.config.AppConfig;
import mitrich.rest.config.MongoConfiguration;
import mitrich.rest.model.User;
import mitrich.rest.repository.UserRepository;
import mitrich.rest.security.JwtTokenUtil;
import mitrich.rest.security.config.WebSecurityConfig;
import mitrich.rest.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, MongoConfiguration.class, WebSecurityConfig.class })
@WebAppConfiguration
public class UserControllerTest {

	private String invalidUserJson = "{}";
	private String validUserJson = "{\"userName\":\"user\",\"password\":\"password\"}";

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private UserService userService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserRepository userRepository;

	@Before
	public void setupMockMvcInstance() {
		RestAssuredMockMvc.webAppContextSetup(wac);
		userRepository.deleteAll();
	}

	@After
	public void resetMockMvcInstance() {
		RestAssuredMockMvc.reset();
		userRepository.deleteAll();
	}

	/*------------------------------ Testing createUser() method ---------------------------------*/

	@Test
	public void givenNotValidUser_whenCreateUser_then400IsReceived() {

		given().contentType("application/json").body(invalidUserJson).when().post("/users/").then().statusCode(400);
	}

	@Test
	public void givenValidUser_whenCreateUser_then201IsReceived() {

		given().contentType("application/json").body(validUserJson).when().post("/users/").then().statusCode(201);
	}

	@Test
	public void givenValidUserButAlreadyExistInDB_whenCreateUser_then409IsReceived() throws NoSuchAlgorithmException {

		User user = new User();
		user.setUserName("user");
		user.setPassword(userService.passwordEncode("password" + user.getUserName()));
		user.setRole("ROLE_USER");
		userService.save(user);

		given().contentType("application/json").body(validUserJson).when().post("/users/").then().statusCode(409);
	}

	@Test
	public void givenValidUser_whenCreateUser_thenValidAccessTokenIsReceived() {

		String accessTokenInResponse = given().contentType("application/json").body(validUserJson).when()
				.post("/users/").then().extract().response().asString();

		User user = new User();
		user = userService.findByUserName("user");
		String accessToken = "{\"access_token\":\"" + jwtTokenUtil.generateToken(user) + "\"}";

		assertThat(accessToken, equalTo(accessTokenInResponse));
	}

	/*------------------------------ Testing loginUser() method ---------------------------------*/

	@Test
	public void givenNotValidUser_whenLoginUser_then400IsReceived() {

		given().contentType("application/json").body(invalidUserJson).when().post("/login/").then().statusCode(400);
	}

	@Test
	public void givenValidUserButNotExistInDB_whenLoginUser_then404IsReceived() {

		given().contentType("application/json").body(validUserJson).when().post("/login/").then().statusCode(404);
	}

	@Test
	public void givenValidUserExistInDBButPasswordNotEqual_whenLoginUser_then404IsReceived()
			throws NoSuchAlgorithmException {

		User user = new User();
		user.setUserName("user");
		user.setPassword(userService.passwordEncode("password" + user.getUserName()));
		user.setRole("ROLE_USER");
		userService.save(user);

		String userJSON = "{\"userName\":\"user\",\"password\":\"123456\"}";
		given().contentType("application/json").body(userJSON).when().post("/login/").then().statusCode(404);
	}

	@Test
	public void givenValidUserExistInDB_whenLoginUser_then200IsReceived() throws NoSuchAlgorithmException {

		User user = new User();
		user.setUserName("user");
		user.setPassword(userService.passwordEncode("password" + user.getUserName()));
		user.setRole("ROLE_USER");
		userService.save(user);

		given().contentType("application/json").body(validUserJson).when().post("/login/").then().statusCode(200);
	}

	@Test
	public void givenValidUserExistInDB_whenLoginUser_thenValidAccessTokenIsReceived() throws NoSuchAlgorithmException {

		User user = new User();
		user.setUserName("user");
		user.setPassword(userService.passwordEncode("password" + user.getUserName()));
		user.setRole("ROLE_USER");
		user = userService.save(user);

		String accessTokenInResponse = given().contentType("application/json").body(validUserJson).when()
				.post("/login/").then().extract().response().asString();

		String accessToken = "{\"access_token\":\"" + jwtTokenUtil.generateToken(user) + "\"}";

		assertThat(accessToken, equalTo(accessTokenInResponse));
	}
}
