package mitrich.rest.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UrlService {

	@Autowired
	private UrlRepository urlRepository;

	public void save(Url url) {
		this.urlRepository.save(url);
	}

	public List<Url> findAll() {
		return this.urlRepository.findAll();
	}

	public Url findByShortURL(String shortURL) {

		return this.urlRepository.findByShortURL(shortURL);
	}

	public Url findByLongURL(String longURL) {

		return this.urlRepository.findByLongURL(longURL);
	}

	public List<Url> findByUserName(String userName) {
		return this.urlRepository.findByUserName(userName);
	}

	public List<Url> findByURLTag(String tag) {
		return this.urlRepository.findByURLTag(tag);
	}

}
