package mitrich.rest.controller;

import java.util.List;
import java.util.Random;

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

	@RequestMapping(value = "/url/", method = RequestMethod.POST)
	public Url createURL(@RequestBody Url url) {
		if (this.urlService.findByLongURL(url.getLongURL()) == null) {
			url.setUserName("mit");
			url.setShortURL(randomShortUrl());
			this.urlService.save(url);
			return this.urlService.findByLongURL(url.getLongURL());
		} else
			return url;
	}

	@RequestMapping(value = "/tags/{tag}", method = RequestMethod.GET)
	public List<Url> findURLTag(@PathVariable String tag) {
		return this.urlService.findByURLTag(tag);
	}

	@RequestMapping(value = "/url/{shortURL}", method = RequestMethod.GET)
	public Url findURL(@PathVariable String shortURL) {

		Url url = this.urlService.findByShortURL(shortURL);

		if (url == null) {
			url = new Url();
		}
		return url;
	}

	private String randomShortUrl() {
		Random random = new Random();
		String str;
		do {
			str = "";
			for (int i = 0; i < 6; i++) {
				int n = random.nextInt(62);
				str += (char) (n < 10 ? ('0' + n) : (n > 35 ? ('a' + n - 36) : ('A' + n - 10)));
			}
		} while (this.urlService.findByShortURL(str) != null);

		return str;
	}
}
