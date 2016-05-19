package mitrich.rest.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "urls")
public class Url {

	@Id
	private String id;
	private String shortURL;
	private String longURL;
	private String userName;
	private String description;
	private int redirectCount;
	private List<String> tags;

	public Url() {

	}

	public Url(String shortURL, String longURL, String userName, String description, int redirectCount,
			List<String> tags) {
		super();
		this.shortURL = shortURL;
		this.longURL = longURL;
		this.userName = userName;
		this.description = description;
		this.redirectCount = redirectCount;
		this.tags = tags;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getShortURL() {
		return shortURL;
	}

	public void setShortURL(String shortURL) {
		this.shortURL = shortURL;
	}

	public String getLongURL() {
		return longURL;
	}

	public void setLongURL(String longURL) {
		this.longURL = longURL;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getRedirectCount() {
		return redirectCount;
	}

	public void setRedirectCount(int redirectCount) {
		this.redirectCount = redirectCount;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	@Override
	public String toString() {
		return String.format(
				"Url [id=%s, shortURL=%s, longURL=%s, userName=%s, description=%s, redirectCount=%s, tags=%s]", id,
				shortURL, longURL, userName, description, redirectCount, tags);
	}

}
