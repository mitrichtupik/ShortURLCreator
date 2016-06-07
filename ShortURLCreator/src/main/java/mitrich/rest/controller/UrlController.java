package mitrich.rest.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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

	@RequestMapping(value = "/url/", method = RequestMethod.POST)
	public Url createURL(@RequestBody Url url, HttpServletRequest httpServletRequest) {

		if (urlService.findByLongURLAndUserName(url.getLongURL(), url.getUserName()) == null) {
			url.setShortURL(urlService.randomString(LENGTH_OF_SHORT_URL));
			urlService.save(url);
			return urlService.findByShortURL(url.getShortURL());
		} else
			return url;
	}

	@RequestMapping(value = "/tags/{tag}", method = RequestMethod.GET)
	public List<Url> findURLTag(@PathVariable String tag) {
		return urlService.findByURLTag(tag);
	}

	@RequestMapping(value = "/url/{shortURL}", method = RequestMethod.GET)
	public Url findURL(@PathVariable String shortURL) {

		Url url = urlService.findByShortURL(shortURL);

		if (url == null) {
			url = new Url();
		}
		return url;
	}
}
