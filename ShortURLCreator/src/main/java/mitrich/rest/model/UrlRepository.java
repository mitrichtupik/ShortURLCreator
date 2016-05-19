package mitrich.rest.model;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UrlRepository extends MongoRepository<Url, String> {

	public Url findByShortURL(String shortURL);

	public Url findByLongURL(String longURL);

	public List<Url> findByUserName(String userName);

	@Query("{'tags' : ?0}")
	public List<Url> findByURLTag(String tag);

}
