package mitrich.rest.controller;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

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
import io.restassured.path.json.JsonPath;
import mitrich.rest.config.AppConfig;
import mitrich.rest.config.MongoConfiguration;
import mitrich.rest.model.Url;
import mitrich.rest.model.User;
import mitrich.rest.repository.UrlRepository;
import mitrich.rest.repository.UserRepository;
import mitrich.rest.security.JwtTokenUtil;
import mitrich.rest.security.config.WebSecurityConfig;
import mitrich.rest.service.UrlService;
import mitrich.rest.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, MongoConfiguration.class, WebSecurityConfig.class })
@WebAppConfiguration
public class UrlControllerTest {

	private static final int LENGTH_OF_SHORT_URL = 6;
	private String urlDTOJson = "{\"longURL\":\"https://www.javacodegeeks.com/2015/09/mongodb-shell-guide-operations-and-commands.html\","
			+ "\"description\":\"This is a link to interesting resorce.\","
			+ "\"tags\":[\"mongoDB\",\"NoSQL\",\"spring\"]}";

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private UrlService urlService;

	@Autowired
	private UserService userService;

	@Autowired
	private UrlRepository urlRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Before
	public void setupMockMvcInstance() {
		RestAssuredMockMvc.webAppContextSetup(wac);
		urlRepository.deleteAll();
		userRepository.deleteAll();
	}

	@After
	public void resetMockMvcInstance() {
		RestAssuredMockMvc.reset();
		urlRepository.deleteAll();
		userRepository.deleteAll();
	}

	/*------------------------------ Testing findURL() method ---------------------------------*/

	@Test
	public void givenShortUrlNotExistInDB_whenFindUrl_then404IsReceived() {

		given().when().get("/urls/" + "Af5w6K").then().statusCode(404);
	}

	@Test
	public void givenShortUrlExistInDB_whenFindUrl_then200IsReceived() {

		Url url = new Url();
		url.setShortURL("Af5w6K");
		url.setUserName("user");
		url.setLongURL("https://www.javacodegeeks.com/2015/09/mongodb-shell-guide-operations-and-commands.html");
		url.setDescription("This is a link to interesting resorce.");
		url.setTags(Arrays.asList("mongoDB", "NoSQL", "spring"));
		urlService.save(url);

		given().when().get("/urls/" + "Af5w6K").then().statusCode(200);
	}

	@Test
	public void givenShortUrlExistInDB_whenFindUrl_thenUrlInformationIsReceived() {

		Url url = new Url();
		url.setShortURL("Af5w6K");
		url.setUserName("user");
		url.setLongURL("https://www.javacodegeeks.com/2015/09/mongodb-shell-guide-operations-and-commands.html");
		url.setDescription("This is a link to interesting resorce.");
		url.setTags(Arrays.asList("mongoDB", "NoSQL", "spring"));
		url = urlService.save(url);

		String UrlJson = given().when().get("/urls/" + "Af5w6K").then().extract().response().asString();

		assertThat(url.getShortURL(), equalTo(JsonPath.from(UrlJson).get("shortURL")));
		assertThat(url.getLongURL(), equalTo(JsonPath.from(UrlJson).get("longURL")));
		assertThat(url.getDescription(), equalTo(JsonPath.from(UrlJson).get("description")));
		assertThat(url.getTags().toString(), equalTo(JsonPath.from(UrlJson).get("tags").toString()));
		assertThat(url.getRedirectCount(), equalTo(JsonPath.from(UrlJson).get("redirectCount")));
	}

	/*------------------------------ Testing findURLTag() method ---------------------------------*/

	@Test
	public void givenTagNotExistInDB_whenFindUrlTag_then404IsReceived() {

		given().when().get("/tags/" + "mongoDB").then().statusCode(404);
	}

	@Test
	public void givenTagExistInDB_whenFindUrlTag_then200IsReceived() {

		Url url = new Url();
		url.setShortURL(urlService.randomString(LENGTH_OF_SHORT_URL));
		url.setUserName("user");
		url.setLongURL("https://www.javacodegeeks.com/2015/09/mongodb-shell-guide-operations-and-commands.html");
		url.setDescription("This is a link to interesting resorce.");
		url.setTags(Arrays.asList("mongoDB", "NoSQL", "spring"));
		urlService.save(url);
		url.setShortURL(urlService.randomString(LENGTH_OF_SHORT_URL));
		url.setUserName("user1");
		url.setLongURL("https://www.owasp.org/index.php/REST_Security_Cheat_Sheet");
		url.setDescription("This is another link to resource.");
		url.setTags(Arrays.asList("java", "mongoDB", "REST"));
		urlService.save(url);

		given().when().get("/tags/" + "mongoDB").then().statusCode(200);
	}

	@Test
	public void givenTagExistInDB_whenFindUrlTag_thenListOfUrlInformationIsReceived() {

		Url url = new Url();
		url.setShortURL(urlService.randomString(LENGTH_OF_SHORT_URL));
		url.setUserName("user");
		url.setLongURL("https://www.javacodegeeks.com/2015/09/mongodb-shell-guide-operations-and-commands.html");
		url.setDescription("This is a link to interesting resorce.");
		url.setTags(Arrays.asList("mongoDB", "NoSQL", "spring"));
		urlService.save(url);

		Url url1 = new Url();
		url1.setShortURL(urlService.randomString(LENGTH_OF_SHORT_URL));
		url1.setUserName("user1");
		url1.setLongURL("https://www.owasp.org/index.php/REST_Security_Cheat_Sheet");
		url1.setDescription("This is another link to resource.");
		url1.setTags(Arrays.asList("java", "mongoDB", "REST"));
		urlService.save(url1);

		String TagsJson = given().when().get("/tags/" + "mongoDB").then().extract().response().asString();

		assertThat(JsonPath.from(TagsJson).getList("").size(), equalTo(2));

		assertThat(url.getShortURL(), equalTo(JsonPath.from(TagsJson).get("[0].shortURL")));
		assertThat(url.getLongURL(), equalTo(JsonPath.from(TagsJson).get("[0].longURL")));
		assertThat(url.getDescription(), equalTo(JsonPath.from(TagsJson).get("[0].description")));
		assertThat(url.getTags().toString(), equalTo(JsonPath.from(TagsJson).get("[0].tags").toString()));
		assertThat(url.getRedirectCount(), equalTo(JsonPath.from(TagsJson).get("[0].redirectCount")));

		assertThat(url1.getShortURL(), equalTo(JsonPath.from(TagsJson).get("[1].shortURL")));
		assertThat(url1.getLongURL(), equalTo(JsonPath.from(TagsJson).get("[1].longURL")));
		assertThat(url1.getDescription(), equalTo(JsonPath.from(TagsJson).get("[1].description")));
		assertThat(url1.getTags().toString(), equalTo(JsonPath.from(TagsJson).get("[1].tags").toString()));
		assertThat(url1.getRedirectCount(), equalTo(JsonPath.from(TagsJson).get("[1].redirectCount")));
	}

	/*------------------------------ Testing createURL() method ---------------------------------*/

	@Test
	public void givenUrlExistInDB_whenCreateUrl_then409IsReceived() throws NoSuchAlgorithmException {

		User user = new User();
		user.setUserName("user");
		user.setPassword(userService.passwordEncode("password" + user.getUserName()));
		user.setRole("ROLE_USER");
		user = userService.save(user);

		Url url = new Url();
		url.setShortURL(urlService.randomString(LENGTH_OF_SHORT_URL));
		url.setUserName("user");
		url.setLongURL("https://www.javacodegeeks.com/2015/09/mongodb-shell-guide-operations-and-commands.html");
		url.setDescription("This is a link to interesting resorce.");
		url.setTags(Arrays.asList("mongoDB", "NoSQL", "spring"));
		urlService.save(url);

		given().contentType("application/json").header("Authorization", "Bearer " + jwtTokenUtil.generateToken(user))
				.body(urlDTOJson).when().post("/security/urls/").then().statusCode(409);
	}

	@Test
	public void givenUrl_whenCreateUrl_then201IsReceived() throws NoSuchAlgorithmException {

		User user = new User();
		user.setUserName("user");
		user.setPassword(userService.passwordEncode("password" + user.getUserName()));
		user.setRole("ROLE_USER");
		user = userService.save(user);

		given().contentType("application/json").header("Authorization", "Bearer " + jwtTokenUtil.generateToken(user))
				.body(urlDTOJson).when().post("/security/urls/").then().statusCode(201);
	}

	@Test
	public void givenUrlAndBadAccessToken_whenCreateUrl_then401IsReceived() throws NoSuchAlgorithmException {

		given().contentType("application/json").header("Authorization", "Bearer 123456").body(urlDTOJson).when()
				.post("/security/urls/").then().statusCode(401);
	}
}
