package mitrich.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import mitrich.rest.model.Url;
import mitrich.rest.model.UrlService;

@Controller
@RequestMapping("/")
public class IndexController {

	@RequestMapping(method = RequestMethod.GET)
	public String mainPage(Model model) {
		return "index";
	}

	@Autowired
	public UrlService urlService;

	@RequestMapping(value = "{shortURL}", method = RequestMethod.GET)
	public String findShortUrl(@PathVariable String shortURL) {
		Url url = urlService.findByShortURL(shortURL);
		if (url == null)
			return "view/errorPage";
		else {
			url.setRedirectCount(url.getRedirectCount() + 1);
			urlService.save(url);
			return "redirect:" + url.getLongURL();
		}
	}
}
