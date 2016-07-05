package mitrich.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import mitrich.rest.model.Url;
import mitrich.rest.service.UrlService;

@RestController
public class UrlController {

	@Autowired
	private UrlService urlService;

	private static final int LENGTH_OF_SHORT_URL = 6;

	@RequestMapping(value = "/security/urls/", method = RequestMethod.POST)
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<Url> createURL(@RequestBody Url urlDTO) {

		String authUserName = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getUsername();

		if (urlService.findByLongURLAndUserName(urlDTO.getLongURL(), authUserName) != null) {
			return new ResponseEntity<Url>(HttpStatus.CONFLICT);
		}

		Url url = new Url();
		url.setShortURL(urlService.randomString(LENGTH_OF_SHORT_URL));
		url.setUserName(authUserName);
		url.setLongURL(urlDTO.getLongURL());
		url.setDescription(urlDTO.getDescription());
		url.setTags(urlDTO.getTags());
		url = urlService.save(url);
		return new ResponseEntity<Url>(url, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/security/urls/user/{userName}", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<List<Url>> findAllURLByUserName(@PathVariable String userName) {

		String authUserName = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getUsername();

		if (!(userName.equals(authUserName))) {
			return new ResponseEntity<List<Url>>(HttpStatus.FORBIDDEN);
		}

		List<Url> urls = urlService.findByUserName(userName);

		if (urls == null || urls.isEmpty()) {
			return new ResponseEntity<List<Url>>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<Url>>(urls, HttpStatus.OK);
	}

	@RequestMapping(value = "/security/urls/{shortURL}", method = RequestMethod.PUT)
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<Url> editUrl(@RequestBody Url urlDTO, @PathVariable String shortURL) {

		Url url = urlService.findByShortURL(shortURL);
		if (url == null) {
			return new ResponseEntity<Url>(HttpStatus.NOT_FOUND);
		}

		String authUserName = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getUsername();

		if (!(url.getUserName().equals(authUserName))) {
			return new ResponseEntity<Url>(HttpStatus.FORBIDDEN);
		}

		url.setDescription(urlDTO.getDescription());
		url.setTags(urlDTO.getTags());
		url = urlService.save(url);
		return new ResponseEntity<Url>(url, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/urls/{shortURL}", method = RequestMethod.GET)
	public ResponseEntity<Url> findURL(@PathVariable String shortURL) {

		Url url = urlService.findByShortURL(shortURL);

		if (url == null) {
			return new ResponseEntity<Url>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Url>(url, HttpStatus.OK);
	}

	@RequestMapping(value = "/tags/{tag}", method = RequestMethod.GET)
	public ResponseEntity<List<Url>> findURLTag(@PathVariable String tag) {
		List<Url> tags = urlService.findByURLTag(tag);

		if (tags == null || tags.isEmpty()) {
			return new ResponseEntity<List<Url>>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<Url>>(tags, HttpStatus.OK);
	}

}
