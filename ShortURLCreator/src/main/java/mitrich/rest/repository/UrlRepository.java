package mitrich.rest.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import mitrich.rest.model.Url;

public interface UrlRepository extends MongoRepository<Url, String> {

	public Url findByShortURL(String shortURL);

	public Url findByLongURLAndUserName(String longURL, String userName);

	public List<Url> findByUserName(String userName);

	@Query("{'tags' : ?0}")
	public List<Url> findByURLTag(String tag);

}
