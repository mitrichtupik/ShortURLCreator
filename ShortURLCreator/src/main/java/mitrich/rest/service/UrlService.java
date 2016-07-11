package mitrich.rest.service;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mitrich.rest.model.Url;
import mitrich.rest.repository.UrlRepository;

@Service
public class UrlService {

	@Autowired
	private UrlRepository urlRepository;

	public Url save(Url url) {
		return urlRepository.save(url);
	}

	public List<Url> findAll() {
		return urlRepository.findAll();
	}

	public Url findByShortURL(String shortURL) {

		return urlRepository.findByShortURL(shortURL);
	}

	public Url findByLongURLAndUserName(String longURL, String userName) {

		return urlRepository.findByLongURLAndUserName(longURL, userName);
	}

	public List<Url> findByUserName(String userName) {
		return urlRepository.findByUserName(userName);
	}

	public List<Url> findByURLTag(String tag) {
		return urlRepository.findByURLTag(tag);
	}

	public String randomString(int length) {
		Random random = new Random();
		StringBuilder str;
		do {
			str = new StringBuilder();
			for (int i = 0; i < length; i++) {
				int n = random.nextInt(62);
				str.append((char) (n < 10 ? ('0' + n) : (n > 35 ? ('a' + n - 36) : ('A' + n - 10))));
			}
		} while (findByShortURL(str.toString()) != null);

		return str.toString();
	}

}
