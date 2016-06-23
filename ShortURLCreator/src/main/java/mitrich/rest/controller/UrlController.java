package mitrich.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import mitrich.rest.model.Url;
import mitrich.rest.model.UrlService;

@RestController
public class UrlController {

	@Autowired
	private UrlService urlService;

	private static final int LENGTH_OF_SHORT_URL = 6;

	@RequestMapping(value = "/urls/", method = RequestMethod.POST)
	@PostAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<Url> createURL(@RequestBody Url url) {

		if (urlService.findByLongURLAndUserName(url.getLongURL(), url.getUserName()) != null) {
			return new ResponseEntity<Url>(HttpStatus.CONFLICT);
		}
		url.setShortURL(urlService.randomString(LENGTH_OF_SHORT_URL));
		Url saved_url = urlService.save(url);
		return new ResponseEntity<Url>(saved_url, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/tags/{tag}", method = RequestMethod.GET)
	public ResponseEntity<List<Url>> findURLTag(@PathVariable String tag) {
		List<Url> tags = urlService.findByURLTag(tag);

		if (tags == null || tags.isEmpty()) {
			return new ResponseEntity<List<Url>>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<Url>>(tags, HttpStatus.OK);
	}

	@RequestMapping(value = "/urls/{shortURL}", method = RequestMethod.GET)
	public ResponseEntity<Url> findURL(@PathVariable String shortURL) {

		Url url = urlService.findByShortURL(shortURL);

		if (url == null) {
			return new ResponseEntity<Url>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Url>(url, HttpStatus.OK);
	}

	@RequestMapping(value = "/users/{userName}/urls", method = RequestMethod.GET)
	public ResponseEntity<List<Url>> findAllURLByUserName(@PathVariable String userName) {
		List<Url> urls = urlService.findByUserName(userName);

		if (urls == null || urls.isEmpty()) {
			return new ResponseEntity<List<Url>>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<Url>>(urls, HttpStatus.OK);
	}

}
